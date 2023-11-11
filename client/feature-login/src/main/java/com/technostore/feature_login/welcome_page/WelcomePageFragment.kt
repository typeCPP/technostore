package com.technostore.feature_login.welcome_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.technostore.arch.mvi.State
import com.technostore.feature_login.databinding.WelcomePageFragmentBinding
import com.technostore.feature_login.welcome_page.presentation.WelcomePageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomePageFragment : Fragment() {

    private val viewModel by viewModels<WelcomePageViewModel>()
    private lateinit var binding: WelcomePageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = WelcomePageFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::checkState)
            }
        }
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.nextButton.setOnClickListener {
            viewModel.nextClicked()
        }
    }

    private fun checkState(state: State) {}
}