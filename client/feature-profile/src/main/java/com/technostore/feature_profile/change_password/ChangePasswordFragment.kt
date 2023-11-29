package com.technostore.feature_profile.change_password

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.technostore.arch.mvi.News
import com.technostore.core.R
import com.technostore.feature_profile.change_password.presentation.ChangePasswordNews
import com.technostore.feature_profile.change_password.presentation.ChangePasswordState
import com.technostore.feature_profile.change_password.presentation.ChangePasswordViewModel
import com.technostore.feature_profile.databinding.PasswordChangePageFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private val viewModel by viewModels<ChangePasswordViewModel>()
    private lateinit var binding: PasswordChangePageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = PasswordChangePageFragmentBinding.inflate(inflater)
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
        binding.tilEnterNewPassword.errorIconDrawable = null
        binding.tilEnterOldPassword.errorIconDrawable = null
        binding.tilRepeatNewPassword.errorIconDrawable = null
        setOnClickListenerForNextButton()
        setOnClickListenerForBackButton()
        setTextChangedListeners()
    }

    private fun setOnClickListenerForNextButton() {
        binding.tvButtonNext.setOnClickListener {
            viewModel.onChangedClicked(
                oldPassword = binding.etOldPassword.text.toString(),
                newPassword = binding.etEnterNewPassword.text.toString(),
                newRepeatPassword = binding.etRepeatNewPassword.text.toString()
            )
        }
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBackButton.setOnClickListener {
            viewModel.onBackClicked()
        }
    }

    private fun setTextChangedListeners() {
        binding.etOldPassword.addTextChangedListener(afterTextChanged = {
            binding.tilEnterOldPassword.error = ""
        })
        binding.etEnterNewPassword.addTextChangedListener(afterTextChanged = {
            binding.tilEnterNewPassword.error = ""
        })
        binding.etRepeatNewPassword.addTextChangedListener(afterTextChanged = {
            binding.tilRepeatNewPassword.error = ""
        })
    }


    private fun render(state: ChangePasswordState) {
        binding.loading.clLoadingPage.isVisible = state.isLoading
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            ChangePasswordNews.WrongOldPassword -> {
                binding.tilRepeatNewPassword.error =
                    getString(com.technostore.feature_profile.R.string.change_password_wrong_old_password)
            }

            ChangePasswordNews.ShowErrorToast -> {
                Toast.makeText(context, R.string.error_toast, Toast.LENGTH_SHORT).show()
            }

            ChangePasswordNews.OpenPreviousPage -> {
                findNavController().popBackStack()
            }

            ChangePasswordNews.OldPasswordIsEmpty,
            ChangePasswordNews.NewPasswordIsEmpty,
            ChangePasswordNews.NewRepeatPasswordIsEmpty -> {
                binding.tilRepeatNewPassword.error =
                    getString(com.technostore.feature_profile.R.string.profile_empty_field)
            }

            ChangePasswordNews.PasswordsIsNotEquals -> {
                binding.tilRepeatNewPassword.error =
                    getString(com.technostore.feature_profile.R.string.change_password_passwords_are_broken)
            }
        }
    }
}