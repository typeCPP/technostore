package com.technostore.feature_login.registration_user_info

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.technostore.arch.mvi.News
import com.technostore.feature_login.R
import com.technostore.core.R as CoreR
import com.technostore.feature_login.databinding.LoadingFragmentBinding
import com.technostore.feature_login.databinding.RegistrationUserInformationPageFragmentBinding
import com.technostore.feature_login.registration_user_info.crop.CropContract
import com.technostore.feature_login.registration_user_info.presentation.NameValidation
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoNews
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoState
import com.technostore.feature_login.registration_user_info.presentation.ui.PrepareChangeImageUiEvent
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoViewModel
import com.technostore.core.ui.PhotoCompression
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationUserInfoFragment : Fragment() {

    private val viewModel by viewModels<RegistrationUserInfoViewModel>()
    private lateinit var binding: RegistrationUserInformationPageFragmentBinding
    private lateinit var bindingLoading: LoadingFragmentBinding
    private var uri: Uri? = null

    private var cropImageLauncher: ActivityResultLauncher<CropImageContractOptions>? = null
    private val onChangeImageRelay = MutableSharedFlow<Uri?>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val ars: RegistrationUserInfoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        cropImageLauncher = registerForActivityResult(CropContract()) { result ->
            if (result.isSuccessful) {
                uri = result.uriContent
            }
            onChangeImageRelay.tryEmit(uri)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = RegistrationUserInformationPageFragmentBinding.inflate(inflater)
        this.bindingLoading = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrepareChangeImageUiEvent(
            onImageChange = onChangeImageRelay,
            acceptUri = viewModel::imageChanged,
        ).bind(viewLifecycleOwner)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsQueue.collect(::showNews)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::render)
            }
        }
        initViews()
        onChangeImageRelay.map {
            setImage(uri)
        }
    }

    private fun initViews() {
        PrepareChangeImageUiEvent(
            onImageChange = onChangeImageRelay,
            acceptUri = viewModel::imageChanged,
        ).bind(viewLifecycleOwner)

        binding.tilLastname.errorIconDrawable = null
        binding.tilEnterName.errorIconDrawable = null

        setOnClickListenerForImage()
        setOnClickListenerForNextButton()
        setTextChangedListeners()
    }

    private fun setOnClickListenerForImage() {
        binding.cdAddPicture.setOnClickListener {
            val cropImageContractOptions = CropImageContractOptions(
                uri,
                CropImageOptions(
                    imageSourceIncludeGallery = true,
                    imageSourceIncludeCamera = true,
                    guidelines = CropImageView.Guidelines.ON,
                    toolbarColor = ContextCompat.getColor(
                        requireContext(),
                        CoreR.color.basic_color
                    )
                )
            )
            cropImageLauncher?.launch(cropImageContractOptions)
        }
    }

    private fun setOnClickListenerForNextButton() {
        binding.tvButtonNext.setOnClickListener {
            val uriTemp = uri
            val byteArray = PhotoCompression.compress(requireContext(), uriTemp)
            viewModel.registrationClicked(
                byteArray = byteArray,
                email = ars.email,
                password = ars.password,
                name = binding.etName.text.toString(),
                lastName = binding.etLastname.text.toString()
            )
        }
    }

    private fun setTextChangedListeners() {
        binding.etName.addTextChangedListener(afterTextChanged = {
            binding.tilEnterName.error = ""
        })
        binding.etLastname.addTextChangedListener(afterTextChanged = {
            binding.tilLastname.error = ""
        })
    }

    private fun setImage(uri: Uri?) {
        if (uri != null) {
            binding.ivAddFriends.setPadding(0, 0, 0, 0)
            Glide.with(this)
                .load(uri)
                .into(binding.ivAddFriends)
        } else {
            binding.ivAddFriends.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.add_picture_image
                )
            )
        }
    }

    private fun render(state: RegistrationUserInfoState) {
        bindingLoading.clLoadingPage.isVisible = state.isLoading
        when (state.firstNameValidation) {
            NameValidation.SUCCESS -> binding.tilEnterName.error = ""
            NameValidation.EMPTY -> binding.tilEnterName.error =
                getString(R.string.login_empty_field)

            NameValidation.ERROR -> binding.tilEnterName.error =
                getString(R.string.login_error_max_length)
        }
        when (state.lastNameValidation) {
            NameValidation.SUCCESS -> binding.tilLastname.error = ""
            NameValidation.EMPTY -> binding.tilLastname.error =
                getString(R.string.login_empty_field)

            NameValidation.ERROR -> binding.tilLastname.error =
                getString(R.string.login_error_max_length)
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            RegistrationUserInfoNews.ShowErrorToast -> Toast.makeText(
                context,
                CoreR.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()

            is RegistrationUserInfoNews.ChangeImage -> setImage(news.uri)
            is RegistrationUserInfoNews.OpenCodePage -> {
                val action =
                    RegistrationUserInfoFragmentDirections.actionRegistrationUserInfoFragmentToConfirmationCodeFragment()
                action.email = ars.email
                findNavController().navigate(action)
            }
        }
    }
}