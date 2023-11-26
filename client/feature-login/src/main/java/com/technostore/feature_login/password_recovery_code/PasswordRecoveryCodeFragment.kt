package com.technostore.feature_login.password_recovery_code

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
import androidx.navigation.fragment.navArgs
import com.technostore.arch.mvi.News
import com.technostore.feature_login.R
import com.technostore.core.R as CoreR
import com.technostore.core.databinding.LoadingFragmentBinding
import com.technostore.feature_login.databinding.PasswordRecoveryCodePageFragmentBinding
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeNews
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeState
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordRecoveryCodeFragment : Fragment() {
    private val viewModel by viewModels<PasswordRecoveryCodeViewModel>()
    private lateinit var binding: PasswordRecoveryCodePageFragmentBinding
    private lateinit var bindingLoading: LoadingFragmentBinding
    private val ars: PasswordRecoveryCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = PasswordRecoveryCodePageFragmentBinding.inflate(inflater)
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
                viewModel.viewState.collect(::checkState)
            }
        }
        initViews()
    }

    private fun initViews() {
        setOnClickListenerForNextButton()
        setOnClickListenerForRepeatButton()
    }

    private fun setOnClickListenerForNextButton() {
        binding.tvButtonNext.setOnClickListener {
            viewModel.onNextClicked(email = ars.email, code = binding.etCode.text.toString())
        }
    }

    private fun setOnClickListenerForRepeatButton() {
        binding.tvSkipButton.setOnClickListener {
            viewModel.onRepeatClicked(email = ars.email)
        }
    }

    private fun showNews(news: News) {
        when (news) {
            PasswordRecoveryCodeNews.ShowErrorToast -> Toast.makeText(
                context,
                CoreR.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()

            PasswordRecoveryCodeNews.CodeIsInvalid -> Toast.makeText(
                context,
                R.string.confirm_code_error,
                Toast.LENGTH_SHORT
            ).show()

            PasswordRecoveryCodeNews.CodeErrorLength -> Toast.makeText(
                context,
                R.string.password_recovery_code_error_length,
                Toast.LENGTH_SHORT
            ).show()

            PasswordRecoveryCodeNews.OpenPasswordRecoveryPage -> {
                val action =
                    PasswordRecoveryCodeFragmentDirections.actionPasswordRecoveryCodeFragmentToPasswordRecoveryFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun checkState(state: PasswordRecoveryCodeState) {
        bindingLoading.clLoadingPage.isVisible = state.isLoading
    }
}