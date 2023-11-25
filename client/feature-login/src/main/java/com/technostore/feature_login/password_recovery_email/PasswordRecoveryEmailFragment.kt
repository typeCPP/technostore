package com.technostore.feature_login.password_recovery_email

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.technostore.arch.mvi.News
import com.technostore.feature_login.R
import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.databinding.LoadingFragmentBinding
import com.technostore.feature_login.databinding.PasswordRecoveryEmailPageFragmentBinding
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailNews
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailState
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordRecoveryEmailFragment : Fragment() {
    private val viewModel by viewModels<PasswordRecoveryEmailViewModel>()
    private lateinit var binding: PasswordRecoveryEmailPageFragmentBinding
    private lateinit var bindingLoading: LoadingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = PasswordRecoveryEmailPageFragmentBinding.inflate(inflater)
        this.bindingLoading = binding.loginLoading
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
        binding.tilEmail.errorIconDrawable = null
        setOnClickListenerForNextButton()
    }

    private fun setOnClickListenerForNextButton() {
        binding.tvButtonNext.setOnClickListener {
            viewModel.nextClicked(binding.etEmail.text.toString())
        }
    }

    private fun render(state: PasswordRecoveryEmailState) {
        bindingLoading.clLoadingPage.isVisible = state.isLoading
        when (state.emailValidation) {
            EmailValidation.SUCCESS -> binding.tilEmail.error = ""

            EmailValidation.EMPTY -> binding.tilEmail.error = getString(R.string.login_empty_field)

            EmailValidation.ERROR_MAX_LENGTH -> binding.tilEmail.error =
                getString(R.string.login_email_error_length)

            EmailValidation.ERROR -> binding.tilEmail.error =
                getString(R.string.login_email_error)

            EmailValidation.NOT_EXISTS -> binding.tilEmail.error =
                getString(R.string.login_email_is_not_exists)

            else -> {}
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            is PasswordRecoveryEmailNews.OpenCodePage -> {
                val action =
                    PasswordRecoveryEmailFragmentDirections.actionPasswordRecoveryFragmentToPasswordRecoveryCodeFragment()
                action.email = news.email
                findNavController().navigate(action)
            }

            PasswordRecoveryEmailNews.ShowErrorToast -> Toast.makeText(
                context,
                com.technostore.core.R.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()

        }
    }
}