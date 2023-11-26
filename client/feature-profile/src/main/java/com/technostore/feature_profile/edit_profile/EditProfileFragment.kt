package com.technostore.feature_profile.edit_profile

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
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.technostore.arch.mvi.News
import com.technostore.core.ui.PhotoCompression
import com.technostore.core.ui.crop.CropContract
import com.technostore.core.R as CoreR
import com.technostore.feature_profile.databinding.EditProfilePageFragmentBinding
import com.technostore.core.databinding.LoadingFragmentBinding
import com.technostore.feature_profile.R
import com.technostore.feature_profile.edit_profile.presentation.EditProfileNews
import com.technostore.feature_profile.edit_profile.presentation.EditProfileState
import com.technostore.feature_profile.edit_profile.presentation.EditProfileViewModel
import com.technostore.feature_profile.edit_profile.ui.PrepareChangeImageUiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private val viewModel by viewModels<EditProfileViewModel>()
    private lateinit var binding: EditProfilePageFragmentBinding
    private lateinit var bindingLoading: LoadingFragmentBinding

    private var uri: Uri? = null
    private var cropImageLauncher: ActivityResultLauncher<CropImageContractOptions>? = null
    private val onChangeImageRelay = MutableSharedFlow<Uri?>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


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
        this.binding = EditProfilePageFragmentBinding.inflate(inflater)
        this.bindingLoading = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    private fun initViews() {
        binding.tilEnterName.errorIconDrawable = null
        binding.tilNickname.errorIconDrawable = null
        PrepareChangeImageUiEvent(
            onImageChange = onChangeImageRelay,
            acceptUri = viewModel::imageChanged,
        ).bind(viewLifecycleOwner)
        setOnClickListenerForPictureImage()
        setOnClickListenerForChangeButton()
        setTextChangedListeners()
    }

    private fun setOnClickListenerForPictureImage() {
        binding.ivPicture.setOnClickListener {
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

    private fun setOnClickListenerForChangeButton() {
        binding.tvButtonNext.setOnClickListener {
            val uriTemp = uri
            val byteArray = PhotoCompression.compress(requireContext(), uriTemp)
            viewModel.changedClicked(
                byteArray = byteArray,
                name = binding.etName.text.toString(),
                lastName = binding.etNickname.text.toString()
            )
        }
    }

    private fun setTextChangedListeners() {
        binding.etName.addTextChangedListener(afterTextChanged = {
            binding.tilEnterName.error = ""
        })
        binding.etNickname.addTextChangedListener(afterTextChanged = {
            binding.tilNickname.error = ""
        })
    }

    private fun render(state: EditProfileState) {
        bindingLoading.clLoadingPage.isVisible = state.isLoading
    }

    private fun setImage(uri: Uri?) {
        if (uri != null) {
            binding.ivPicture.setPadding(0, 0, 0, 0)
            Glide.with(this)
                .load(uri)
                .into(binding.ivPicture)
        } else {
            binding.ivPicture.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    CoreR.drawable.add_picture_image
                )
            )
        }
    }


    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            EditProfileNews.ShowErrorToast -> {
                Toast.makeText(context, CoreR.string.error_toast, Toast.LENGTH_SHORT).show()
            }

            EditProfileNews.OpenPreviousPage -> {}
            EditProfileNews.NameIsEmpty -> {
                binding.tilEnterName.error = getString(R.string.profile_empty_field)
            }

            EditProfileNews.LastNameIsEmpty -> {
                binding.tilNickname.error = getString(R.string.profile_empty_field)
            }

            is EditProfileNews.ChangeImage -> setImage(news.uri)
        }
    }
}