package com.technostore.feature_review.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.technostore.arch.mvi.News
import com.technostore.core.R as CoreR
import com.technostore.feature_review.databinding.ReviewFragmentBinding
import com.technostore.feature_review.review.presentation.ReviewNews
import com.technostore.feature_review.review.presentation.ReviewState
import com.technostore.feature_review.review.presentation.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewFragment : Fragment() {
    private val viewModel by viewModels<ReviewViewModel>()
    private lateinit var binding: ReviewFragmentBinding
    private val args: ReviewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = ReviewFragmentBinding.inflate(inflater)
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
        viewModel.init(
            userName = args.userName,
            photoLink = args.photoLink,
            date = args.date,
            rating = args.rating,
            text = args.text
        )
        initViews()
    }

    private fun initViews() {
        setOnClickListenerForBackButton()
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivPrev.setOnClickListener {
            viewModel.onClickBack()
        }
    }


    private fun render(state: ReviewState) {
        with(binding) {
            Glide.with(ivUserPhoto)
                .load(state.photoLink)
                .centerCrop()
                .placeholder(
                    ContextCompat.getDrawable(
                        requireContext(),
                        CoreR.drawable.icon_default_user
                    )
                )
                .into(ivUserPhoto)
            setRatingBackground(state.rating)
            tvUserName.text = state.userName
            tvRating.text = state.rating.toString()
            tvDate.text = state.date
            text.text = state.text
        }
    }

    private fun setRatingBackground(rating: Int) {
        with(binding) {
            when (rating) {
                in 1..3 -> clRating.background = ContextCompat.getDrawable(
                    requireContext(),
                    CoreR.drawable.negative_rating_background
                )

                in 4..6 -> clRating.background = ContextCompat.getDrawable(
                    requireContext(),
                    CoreR.drawable.neural_rating_background
                )

                in 7..10 -> clRating.background = ContextCompat.getDrawable(
                    requireContext(),
                    CoreR.drawable.positive_rating_background
                )
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            ReviewNews.OpenPreviousPage -> {
                findNavController().popBackStack()
            }
        }
    }
}