package com.technostore.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.technostore.arch.mvi.News
import com.technostore.base.databinding.BaseFragmentBinding
import com.technostore.base.presentation.BaseNews
import com.technostore.base.presentation.BaseViewModel
import com.technostore.navigation.NavigationFlow
import com.technostore.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BaseFragment : Fragment() {

    private val viewModel by viewModels<BaseViewModel>()

    private lateinit var binding: BaseFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = BaseFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newsQueue.collect(::showNews)
            }
        }
        viewModel.init()
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        val activity = (activity as ToFlowNavigatable)
        when (news) {
            is BaseNews.OpenLogin -> {
                activity.navigateToFlow(NavigationFlow.LoginFlow)
            }

            is BaseNews.OpenOnboarding -> {
                activity.navigateToFlow(NavigationFlow.OnboardingFlow)
            }

            is BaseNews.OpenMainPage -> {
                activity.navigateToAnotherActivity()
            }
        }
    }
}