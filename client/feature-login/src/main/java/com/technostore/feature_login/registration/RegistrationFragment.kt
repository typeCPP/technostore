package com.technostore.feature_login.registration

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
import com.technostore.feature_login.common_ui.PasswordValidation
import com.technostore.feature_login.databinding.LoadingFragmentBinding
import com.technostore.feature_login.databinding.RegistrationPageFragmentBinding
import com.technostore.feature_login.registration.presentation.RegistrationNews
import com.technostore.feature_login.registration.presentation.RegistrationState
import com.technostore.feature_login.registration.presentation.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private val viewModel by viewModels<RegistrationViewModel>()
    private lateinit var binding: RegistrationPageFragmentBinding
    private lateinit var bindingLoading: LoadingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = RegistrationPageFragmentBinding.inflate(inflater)
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
        binding.tilPassword.errorIconDrawable = null
        binding.tilPassword2.errorIconDrawable = null
        setOnClickListenerForSignInButton()
        setonClickListenerForSignInButton()
    }

    private fun setOnClickListenerForSignInButton() {
        binding.tvButtonNext.setOnClickListener {
            viewModel.registrationClicked(
                email = binding.etEmail.text.toString(),
                firstPassword = binding.etPassword.text.toString(),
                secondPassword = binding.etPassword2.text.toString()
            )
        }
    }

    private fun setonClickListenerForSignInButton() {
        binding.tvSignin.setOnClickListener {
            viewModel.loginClicked()
        }
    }

    private fun render(state: RegistrationState) {
        bindingLoading.clLoadingPage.isVisible = state.isLoading
        when (state.emailValidation) {
            EmailValidation.SUCCESS -> binding.tilEmail.error = ""

            EmailValidation.EMPTY -> binding.tilEmail.error = getString(R.string.login_empty_field)

            EmailValidation.ERROR_MAX_LENGTH -> binding.tilEmail.error =
                getString(R.string.sign_in_email_is_exists)

            EmailValidation.ERROR -> binding.tilEmail.error =
                getString(R.string.login_email_error)

            EmailValidation.EXISTS -> binding.tilEmail.error =
                getString(R.string.registration_email_exists)

            else -> {}
        }
        when (state.firstPasswordValidation) {
            PasswordValidation.SUCCESS -> binding.tilPassword.error = ""
            PasswordValidation.EMPTY -> binding.tilPassword.error =
                getString(R.string.login_empty_field)

            PasswordValidation.ERROR_SYMBOL -> binding.tilPassword.error =
                getString(R.string.registration_password_error_symbols)

            PasswordValidation.ERROR_MIN_LENGTH -> binding.tilPassword.error =
                getString(R.string.registration_password_error_min_length)

            PasswordValidation.ERROR_MAX_LENGTH -> binding.tilPassword.error =
                getString(R.string.login_error_max_length)

            else -> {}
        }
        when (state.secondPasswordValidation) {
            PasswordValidation.SUCCESS -> binding.tilPassword2.error = ""
            PasswordValidation.EMPTY -> binding.tilEmail.error =
                getString(R.string.login_empty_field)

            PasswordValidation.ERROR_SYMBOL -> binding.tilPassword2.error =
                getString(R.string.registration_password_error_symbols)

            PasswordValidation.ERROR_MIN_LENGTH -> binding.tilPassword2.error =
                getString(R.string.registration_password_error_min_length)

            PasswordValidation.ERROR_MAX_LENGTH -> binding.tilPassword2.error =
                getString(R.string.login_error_max_length)

            PasswordValidation.DIFFERENT -> binding.tilPassword2.error =
                getString(R.string.registration_passwords_are_broken)

            else -> {}
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            RegistrationNews.ShowErrorToast -> Toast.makeText(
                context,
                com.technostore.core.R.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()

            RegistrationNews.OpenSignInPage -> findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            )

            is RegistrationNews.OpenRegistrationDataPage -> {
                val action =
                    RegistrationFragmentDirections.actionRegistrationFragmentToRegistrationUserInfoFramgent()
                action.email = news.email
                action.password = news.password
                findNavController().navigate(action)
            }
        }
    }
}