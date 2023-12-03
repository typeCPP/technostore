package com.technostore.feature_product.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technostore.arch.mvi.News
import com.technostore.feature_product.R
import com.technostore.feature_product.databinding.ProductFragmentBinding
import com.technostore.feature_product.product.presentation.ProductNews
import com.technostore.feature_product.product.presentation.ProductState
import com.technostore.feature_product.product.presentation.ProductViewModel
import com.technostore.feature_product.product.ui.RateProductDialog
import com.technostore.feature_product.product.ui.ReviewAdapter
import com.technostore.navigation.NavigationFlow
import com.technostore.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.technostore.core.R as CoreR


@AndroidEntryPoint
class ProductDetailFragment : Fragment() {
    private val viewModel by viewModels<ProductViewModel>()
    private lateinit var binding: ProductFragmentBinding
    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = ProductFragmentBinding.inflate(inflater)
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
        setAdapterForRecyclerView()
        setOnClickListenerForBackButton()
        setOnClickListenerForMoreReviewsButton()
        setOnClickListenerForMoreDescriptionButton()
        setOnClickListenerForBuyButton()
        setOnClickListenerForRateButton()
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBack.setOnClickListener {
            viewModel.onClickBack()
        }
    }

    private fun setOnClickListenerForMoreReviewsButton() {
        binding.tvReviewMore.setOnClickListener {
            viewModel.onMoreReviewsClicked(args.productId)
        }
    }

    private fun setOnClickListenerForMoreDescriptionButton() {
        binding.tvDescriptionMore.setOnClickListener {
            viewModel.onMoreDescriptionClicked()
        }
    }

    private fun setOnClickListenerForBuyButton() {
        binding.clBuy.setOnClickListener {
            viewModel.onBuyClicked(args.productId)
        }
    }

    private fun setOnClickListenerForRateButton() {
        binding.clRating.setOnClickListener {
            viewModel.onRateClicked()
        }
    }

    private fun setAdapterForRecyclerView() {
        val adapter = ReviewAdapter()
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvReview.layoutManager = layoutManager
        binding.rvReview.adapter = adapter
    }

    private fun render(state: ProductState) {
        with(binding) {
            layoutShimmer.slShimmerProduct.isVisible =
                state.isLoading || state.productDetail == null
            setVisibilityForMainContent(state.isLoading || state.productDetail == null)
            if (state.productDetail != null) {
                initReview(state)
                category.text = state.productDetail.category.name
                tvDescription.text = state.productDetail.description
                price.text =
                    requireContext().getString(
                        CoreR.string.price,
                        state.productDetail.price.toString()
                    )
                productName.text = state.productDetail.name
                Glide.with(productImage)
                    .load(state.productDetail.photoLink)
                    .centerCrop()
                    .placeholder(
                        ContextCompat.getDrawable(
                            requireContext(),
                            com.technostore.core.R.drawable.icon_default_product
                        )
                    )
                    .into(productImage)
                if (state.productDetail.userRating == 0) {
                    ratingTitle.text = getString(R.string.product_rating)
                } else {
                    ratingTitle.text = getString(R.string.product_repeat_rating)
                }
            }
        }
    }

    private fun setVisibilityForMainContent(isLoading: Boolean) {
        with(binding) {
            productImage.isVisible = !isLoading
            productName.isVisible = !isLoading
            price.isVisible = !isLoading
            binding.clBuy.isVisible = !isLoading
            binding.clRating.isVisible = !isLoading
            binding.bottomSheet.isVisible = !isLoading
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initReview(state: ProductState) {
        if (state.productDetail!!.reviews.isEmpty()) {
            binding.rvReview.isVisible = false
            binding.tvReviewTitle.isVisible = false
            binding.tvReviewMore.isVisible = false
        } else {
            binding.rvReview.isVisible = true
            binding.tvReviewTitle.isVisible = true
            binding.tvReviewMore.isVisible = true
            binding.tvReviewTitle.text = requireContext().getString(
                R.string.product_review_title,
                state.productDetail.reviews.size
            )
            val adapter = binding.rvReview.adapter as ReviewAdapter
            adapter.submitList(state.productDetail.reviews)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setOnClickListenerForRateDialog(rating: Int, text: String?) {
        viewModel.setReview(rating, text)
    }

    private fun rateDialogShowError() {
        val toast = Toast(requireContext())
        val toastView: View =
            LayoutInflater.from(context).inflate(R.layout.toast_layout, null)
        toast.view = toastView
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            ProductNews.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    CoreR.string.error_toast,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            ProductNews.OpenPreviousPage -> {
                findNavController().popBackStack()
            }

            is ProductNews.OpenDescription -> {
                val action =
                    ProductDetailFragmentDirections.actionProductDetailFragmentToProductDescriptionFragment()
                action.productDescription = news.description
                action.productName = news.productName
                findNavController().navigate(action)
            }

            is ProductNews.OpenRateDialog -> {
                val dialog = RateProductDialog(
                    { rating, text -> setOnClickListenerForRateDialog(rating, text) },
                    { rateDialogShowError() },
                    userReviewText = news.reviewText,
                    userRating = news.userRating
                )
                dialog.show(parentFragmentManager, "rate")
            }

            is ProductNews.OpenReviewsListPage -> {
                (activity as ToFlowNavigatable).navigateToFlow(NavigationFlow.ReviewListFlow(news.productId))
            }
        }
    }
}