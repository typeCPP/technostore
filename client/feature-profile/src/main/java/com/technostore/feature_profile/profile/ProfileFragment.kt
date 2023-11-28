package com.technostore.feature_profile.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.technostore.arch.mvi.News
import com.technostore.feature_profile.databinding.ProfilePageFragmentBinding
import com.technostore.core.R as CoreR
import com.technostore.feature_profile.profile.presentation.ProfileNews
import com.technostore.feature_profile.profile.presentation.ProfileState
import com.technostore.feature_profile.profile.presentation.ProfileViewModel
import com.technostore.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var binding: ProfilePageFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = ProfilePageFragmentBinding.inflate(inflater)
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
        viewModel.init()
        initViews()
    }

    private fun initViews() {
        setOnClickListenerForChangePassword()
        setOnClickListenerForEditProfile()
        setOnClickListenerForLogout()
        setOnClickListenerForOrdersHistory()
    }

    private fun setOnClickListenerForChangePassword() {
        binding.clChangePassword.setOnClickListener {
            viewModel.editPasswordClicked()
        }
    }

    private fun setOnClickListenerForEditProfile() {
        binding.ivPencil.setOnClickListener {
            viewModel.editProfileClicked()
        }
    }

    private fun setOnClickListenerForOrdersHistory() {
        binding.historyOrder.setOnClickListener {
            viewModel.orderHistoryClicked()
        }
    }

    private fun setOnClickListenerForLogout() {
        binding.clLogOut.setOnClickListener {
            viewModel.logoutClicked()
        }
    }

    private fun showNews(news: News) {
        when (news) {
            ProfileNews.OpenChangePasswordPage -> {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
                )
            }

            is ProfileNews.OpenChangeProfilePage -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
                action.name = news.name
                action.lastName = news.lastName
                action.photoUrl = news.photoUrl
                findNavController().navigate(action)
            }

            ProfileNews.OpenOrderHistoryPage -> {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
                )
            }

            ProfileNews.ShowErrorToast -> {
                Toast.makeText(context, CoreR.string.error_toast, Toast.LENGTH_SHORT).show()
            }

            ProfileNews.Logout -> {
                (requireActivity() as ToFlowNavigatable).navigateToAnotherActivity()
            }
        }
    }

    private fun checkState(state: ProfileState) {
        binding.slShimmer.isVisible = state.isLoading
        setVisibilityForContent(state.isLoading)
        Glide.with(this)
            .load(state.image)
            .centerCrop()
            .placeholder(
                ContextCompat.getDrawable(
                    requireContext(),
                    CoreR.drawable.icon_default_user
                )
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.ivPicture)
        binding.tvName.text = state.name
        binding.tvEmail.text = state.email
    }

    private fun setVisibilityForContent(isLoading: Boolean) {
        with(binding) {
            cdPicture.isVisible = !isLoading
            mainActivity.isVisible = !isLoading
            historyOrder.isVisible = !isLoading
            clChangePassword.isVisible = !isLoading
            clLogOut.isVisible = !isLoading
        }
    }
}