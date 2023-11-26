package com.technostore.feature_login.sign_in

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
import com.technostore.feature_login.R
import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.common_ui.PasswordValidation
import com.technostore.core.R as CoreR
import com.technostore.core.databinding.LoadingFragmentBinding
import com.technostore.feature_login.databinding.SignInFragmentBinding
import com.technostore.feature_login.sign_in.presentation.SignInNews
import com.technostore.feature_login.sign_in.presentation.SignInState
import com.technostore.feature_login.sign_in.presentation.SignInViewModel
import com.technostore.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var binding: SignInFragmentBinding
    private lateinit var bindingLoading: LoadingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = SignInFragmentBinding.inflate(inflater)
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
        setOnClickListenerForSignInButton()
        setOnClickListenerForForgotPasswordButton()
        setonClickListenerForRegisterButton()
        setTextChangedListeners()
    }

    private fun setOnClickListenerForSignInButton() {
        binding.tvButtonSignIn.setOnClickListener {
            viewModel.signInClicked(
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
    }

    private fun setTextChangedListeners() {
        binding.etEmail.addTextChangedListener(afterTextChanged = {
            binding.tilEmail.error = ""
        })
        binding.etPassword.addTextChangedListener(afterTextChanged = {
            binding.tilPassword.error = ""
        })
    }

    private fun setOnClickListenerForForgotPasswordButton() {
        binding.tvForgotPassword.setOnClickListener {
            viewModel.forgotPasswordClicked()
        }
    }

    private fun setonClickListenerForRegisterButton() {
        binding.tvRegister.setOnClickListener {
            viewModel.registrationClicked()
        }
    }

    private fun render(state: SignInState) {
        bindingLoading.clLoadingPage.isVisible = state.isLoading
        when (state.emailValidation) {
            EmailValidation.SUCCESS -> binding.tilEmail.error = ""
            EmailValidation.EMPTY -> binding.tilEmail.error = getString(R.string.login_empty_field)
            EmailValidation.NOT_EXISTS -> getString(R.string.login_email_is_not_exists)
            EmailValidation.ERROR -> binding.tilEmail.error =
                getString(R.string.login_email_error)

            else -> {}
        }
        when (state.passwordValidation) {
            PasswordValidation.SUCCESS -> binding.tilPassword.error = ""
            PasswordValidation.EMPTY -> binding.tilPassword.error =
                getString(R.string.login_empty_field)

            PasswordValidation.INCORRECT_PASSWORD -> binding.tilPassword.error =
                getString(R.string.sign_in_invalid_password)

            PasswordValidation.ERROR_MAX_LENGTH -> binding.tilPassword.error =
                getString(R.string.login_error_max_length)

            else -> {}
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            is SignInNews.ShowErrorToast -> Toast.makeText(
                context,
                CoreR.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()

            is SignInNews.OpenMainPage -> {
                val activity = (activity as ToFlowNavigatable)
                activity.navigateToAnotherActivity()
            }

            is SignInNews.OpenPasswordRecoveryPage -> findNavController().navigate(
                SignInFragmentDirections.actionRegistrationFragmentToPasswordRecoveryEmailFramgent()
            )

            is SignInNews.OpenRegistrationPage -> findNavController().navigate(
                SignInFragmentDirections.actionSignInFragmentToRegistrationFragment()
            )
        }
    }
}