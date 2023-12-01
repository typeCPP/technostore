package com.technostore.review_list

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technostore.arch.mvi.News
import com.technostore.business.model.ReviewModel
import com.technostore.feature_review.R
import com.technostore.core.R as CoreR
import com.technostore.feature_review.databinding.ReviewListFragmentBinding
import com.technostore.review_list.presentation.ReviewListNews
import com.technostore.review_list.presentation.ReviewListState
import com.technostore.review_list.presentation.ReviewListViewModel
import com.technostore.review_list.ui.ReviewListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviewListFragment : Fragment() {
    private val viewModel by viewModels<ReviewListViewModel>()
    private lateinit var binding: ReviewListFragmentBinding
    private val args: ReviewListFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = ReviewListFragmentBinding.inflate(inflater)
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
        viewModel.init(args.productId)
        initViews()
    }

    private fun initViews() {
        setOnClickListenerForBackButton()
        setAdapterForRecyclerView()
        setOnClickListenerForAllReviews()
        setOnClickListenerForPositiveReviews()
        setOnClickListenerForNegativeReviews()
        setOnClickListenerForNeutralReviews()
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBack.setOnClickListener {
            viewModel.onClickBack()
        }
    }

    private fun setAdapterForRecyclerView() {
        val adapter = ReviewListAdapter { item -> onClickReview(item) }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvReviews.layoutManager = layoutManager
        binding.rvReviews.adapter = adapter
    }

    private fun onClickReview(review: ReviewModel) {
        viewModel.onReviewClicked(review)
    }

    private fun setOnClickListenerForAllReviews() {
        binding.allReviewsButton.setOnClickListener {
            viewModel.onAllReviewsClicked()
        }
    }

    private fun setOnClickListenerForPositiveReviews() {
        binding.positiveReviewsButton.setOnClickListener {
            viewModel.onPositiveReviewsClicked()
        }
    }

    private fun setOnClickListenerForNegativeReviews() {
        binding.negativeReviewsButton.setOnClickListener {
            viewModel.onNegativeReviewsClicked()
        }
    }

    private fun setOnClickListenerForNeutralReviews() {
        binding.neutralReviewsButton.setOnClickListener {
            viewModel.onNeutralReviewsClicked()
        }
    }

    private fun render(state: ReviewListState) {
        with(binding) {
            layoutShimmer.slShimmer.isVisible = state.isLoading
            setVisibilityForMainContent(state.isLoading)
            calculateAllReviewsButtonText(state.allReviews.size)
        }
    }

    private fun calculateAllReviewsButtonText(allReviewsCount: Int) {
        binding.tvAllReviews.text = if (allReviewsCount > 9999) {
            getString(R.string.review_list_all_reviews, (allReviewsCount / 1000.0).toString() + "k")
        } else {
            getString(R.string.review_list_all_reviews, allReviewsCount.toString())
        }
    }

    private fun setVisibilityForMainContent(isLoading: Boolean) {
        with(binding) {
            allReviewsButton.isVisible = !isLoading
            negativeReviewsButton.isVisible = !isLoading
            positiveReviewsButton.isVisible = !isLoading
            neutralReviewsButton.isVisible = !isLoading
            rvReviews.isVisible = !isLoading
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            ReviewListNews.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    CoreR.string.error_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ReviewListNews.OpenReviewPage -> {
                val action = ReviewListFragmentDirections.actionReviewListFragmentToReviewFragment()
                action.userName = news.review.userName
                action.photoLink = news.review.photoLink
                action.date = news.review.date
                action.rating = news.review.rate
                action.text = news.review.text
                findNavController().navigate(action)
            }

            ReviewListNews.OpenPreviousPage -> {
                findNavController().popBackStack()
            }

            is ReviewListNews.ShowReviews -> {
                val adapter = binding.rvReviews.adapter as ReviewListAdapter
                adapter.submitList(news.reviews)
                binding.rvReviews.smoothScrollToPosition(0)
            }
        }
    }
}