package com.technostore.feature_login.confirm_code

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeNews
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeState
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeViewModel
import com.technostore.feature_login.databinding.ConfirmCodePageFragmentBinding
import com.technostore.core.databinding.LoadingFragmentBinding
import com.technostore.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfirmationCodeFragment : Fragment() {
    private val viewModel by viewModels<ConfirmationCodeViewModel>()
    private lateinit var binding: ConfirmCodePageFragmentBinding
    private lateinit var editTextList: List<EditText>
    private lateinit var bindingLoading: LoadingFragmentBinding
    private val ars: ConfirmationCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = ConfirmCodePageFragmentBinding.inflate(inflater)
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
                viewModel.viewState.collect(::checkState)
            }
        }
        initViews()
    }

    private fun initViews() {
        setOnClickListenerForBackButton()
        setListenerForLastEditText()
        setCodeEditTextListeners()
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setListenerForLastEditText() {
        binding.etCodeSymbol6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                var code = ""
                for (i in editTextList) {
                    code += i.text.toString()
                }
                editTextList[0].text.toString()
                viewModel.checkRecoveryCode(
                    email = ars.email,
                    code = code
                )
            }
        })
    }

    private fun setCodeEditTextListeners() {
        with(binding) {
            editTextList = listOf(
                etCodeSymbol1,
                etCodeSymbol2,
                etCodeSymbol3,
                etCodeSymbol4,
                etCodeSymbol5,
                etCodeSymbol6
            )
        }

        for (i in editTextList.indices) {
            editTextList[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    if (p0 != null) {
                        if (p0.length == 1 && i != editTextList.lastIndex) {
                            editTextList[i + 1].requestFocus()
                        } else if (p0.isEmpty() && i != 0) {
                            editTextList[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun showNews(news: News) {
        when (news) {
            ConfirmationCodeNews.ShowErrorToast -> Toast.makeText(
                context,
                CoreR.string.error_toast,
                Toast.LENGTH_SHORT
            ).show()

            ConfirmationCodeNews.CodeIsInvalid -> Toast.makeText(
                context,
                R.string.confirm_code_error,
                Toast.LENGTH_SHORT
            ).show()

            ConfirmationCodeNews.OpenMainPage -> {
                val activity = (activity as ToFlowNavigatable)
                activity.navigateToAnotherActivity()
            }
        }
    }

    private fun checkState(state: ConfirmationCodeState) {
        bindingLoading.clLoadingPage.isVisible = state.isLoading
    }
}