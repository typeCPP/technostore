package com.technostore.feature_login.password_recovery

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.technostore.arch.mvi.News
import com.technostore.core.R as CoreR
import com.technostore.feature_login.R
import com.technostore.feature_login.common_ui.PasswordValidation
import com.technostore.feature_login.databinding.PasswordRecoveryPageFragmentBinding
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryNews
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryState
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryViewModel
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeNews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordRecoveryFragment : Fragment() {
    private val viewModel by viewModels<PasswordRecoveryViewModel>()
    private lateinit var binding: PasswordRecoveryPageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = PasswordRecoveryPageFragmentBinding.inflate(inflater)
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
        binding.tilPassword.errorIconDrawable = null
        binding.tilPassword2.errorIconDrawable = null
        setOnClickListenerForNextButton()
        setTextChangedListeners()
    }

    private fun setOnClickListenerForNextButton() {
        binding.tvButtonConfirm.setOnClickListener {
            viewModel.nextClicked(
                firstPassword = binding.etPassword.text.toString(),
                secondPassword = binding.etPassword2.text.toString()
            )
        }
    }

    private fun setTextChangedListeners() {
        binding.etPassword.addTextChangedListener(afterTextChanged = {
            binding.tilPassword.error = ""
        })
        binding.etPassword2.addTextChangedListener(afterTextChanged = {
            binding.tilPassword2.error = ""
        })
    }


    private fun render(state: PasswordRecoveryState) {
        when (state.firstPasswordValidation) {
            PasswordValidation.SUCCESS -> binding.tilPassword.error = ""
            PasswordValidation.EMPTY -> binding.tilPassword.error =
                getString(R.string.login_empty_field)

            PasswordValidation.ERROR_SYMBOL -> binding.tilPassword.error =
                getString(R.string.login_password_error_symbols)

            PasswordValidation.ERROR_MIN_LENGTH -> binding.tilPassword.error =
                getString(R.string.login_password_error_min_length)

            PasswordValidation.ERROR_MAX_LENGTH -> binding.tilPassword.error =
                getString(R.string.login_error_max_length)

            else -> {}
        }
        when (state.secondPasswordValidation) {
            PasswordValidation.SUCCESS -> binding.tilPassword2.error = ""
            PasswordValidation.EMPTY -> binding.tilPassword2.error =
                getString(R.string.login_empty_field)

            PasswordValidation.ERROR_SYMBOL -> binding.tilPassword2.error =
                getString(R.string.login_password_error_symbols)

            PasswordValidation.ERROR_MIN_LENGTH -> binding.tilPassword2.error =
                getString(R.string.login_password_error_min_length)

            PasswordValidation.ERROR_MAX_LENGTH -> binding.tilPassword2.error =
                getString(R.string.login_error_max_length)

            PasswordValidation.DIFFERENT -> binding.tilPassword2.error =
                getString(R.string.login_passwords_are_broken)

            else -> {}
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            is PasswordRecoveryNews.OpenLoginPage -> findNavController().navigate(
                PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToLoginFragment()
            )

            PasswordRecoveryCodeNews.ShowErrorToast -> Toast.makeText(
                context,
                CoreR.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}