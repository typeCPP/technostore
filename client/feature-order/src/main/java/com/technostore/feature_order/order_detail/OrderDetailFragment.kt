package com.technostore.feature_order.order_detail

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
import com.technostore.feature_order.R
import com.technostore.feature_order.databinding.OrderDetailsFragmentBinding
import com.technostore.feature_order.order_detail.presentation.OrderDetailNews
import com.technostore.feature_order.order_detail.presentation.OrderDetailState
import com.technostore.feature_order.order_detail.presentation.OrderDetailViewModel
import com.technostore.feature_order.order_detail.ui.OrderDetailAdapter
import com.technostore.navigation.MainNavGraphDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
    private val viewModel by viewModels<OrderDetailViewModel>()
    private lateinit var binding: OrderDetailsFragmentBinding
    private val args: OrderDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = OrderDetailsFragmentBinding.inflate(inflater)
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
        viewModel.init(args.orderId)
        initViews()
    }

    private fun initViews() {
        setAdapterForRecyclerView()
        setOnClickListenerForBackButton()
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBackButton.setOnClickListener {
            viewModel.onBackClicked()
        }
    }


    private fun setAdapterForRecyclerView() {
        val adapter = OrderDetailAdapter { item -> onClickProduct(item) }
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.productsList.layoutManager = layoutManager
        binding.productsList.adapter = adapter
    }

    private fun onClickProduct(product: com.technostore.feature_order.business.model.ProductOrderModel) {
        viewModel.productClicked(product.id)
    }

    private fun render(state: OrderDetailState) {
        with(binding) {
            if (!state.isLoading) {
                tvTitle.text =
                    getString(R.string.orders_item_title, state.orderDetail?.id.toString())
                tvTitle.isVisible = true
            } else {
                tvTitle.isVisible = false
            }
            layoutShimmer.slShimmer.isVisible = state.isLoading
            productsList.isVisible = !state.isLoading
            val adapter = binding.productsList.adapter as OrderDetailAdapter
            adapter.submitList(state.orderDetail?.products.orEmpty())
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            OrderDetailNews.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    com.technostore.core.R.string.error_toast,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            OrderDetailNews.OpenPreviousPage -> {
                findNavController().popBackStack()
            }

            is OrderDetailNews.OpenProductPage -> {
                val action = MainNavGraphDirections.actionGlobalProductFlow()
                action.productId = news.id
                findNavController().navigate(action)
            }
        }
    }
}