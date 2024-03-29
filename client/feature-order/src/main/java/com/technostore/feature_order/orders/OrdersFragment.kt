package com.technostore.feature_order.orders

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technostore.arch.mvi.News
import com.technostore.feature_order.R
import com.technostore.feature_order.databinding.OrdersFragmentBinding
import com.technostore.core.R as CoreR
import com.technostore.feature_order.orders.presentation.OrdersNews
import com.technostore.feature_order.orders.presentation.OrdersState
import com.technostore.feature_order.orders.presentation.OrdersViewModel
import com.technostore.feature_order.orders.ui.OrdersAdapter
import com.technostore.navigation.BottomNavigatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    private val viewModel by viewModels<OrdersViewModel>()
    private lateinit var binding: OrdersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = OrdersFragmentBinding.inflate(inflater)
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
        viewModel.init()
        initViews()
    }

    private fun initViews() {
        setAdapterForRecyclerView()
        setOnClickListenerForBackButton()
        setOnClickListenerForBuyButton()
    }

    private fun setOnClickListenerForBackButton() {
        binding.ivBackButton.setOnClickListener {
            viewModel.onBackClicked()
        }
    }

    private fun setOnClickListenerForBuyButton() {
        val button = view?.findViewById<AppCompatTextView>(CoreR.id.button)
        button?.setOnClickListener {
            viewModel.onGoToShoppingClicked()
        }
    }


    private fun setAdapterForRecyclerView() {
        val adapter = OrdersAdapter { item -> onClickOrder(item) }
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.ordersList.layoutManager = layoutManager
        binding.ordersList.adapter = adapter
    }

    private fun onClickOrder(id: Long) {
        viewModel.orderClick(id)
    }

    private fun render(state: OrdersState) {
        with(binding) {
            slShimmer.isVisible = state.isLoading
            emptyOrders.clMain.isVisible = !state.isLoading && state.ordersList.isEmpty()
            renderEmptyOrders()
            ordersList.isVisible = !state.isLoading && state.ordersList.isNotEmpty()
            val adapter = binding.ordersList.adapter as OrdersAdapter
            adapter.submitList(state.ordersList)
        }
    }

    private fun renderEmptyOrders() {
        with(binding) {
            emptyOrders.title.text = getString(R.string.orders_history_empty_title)
            emptyOrders.description.text = getString(R.string.orders_history_empty_subtitle)
            emptyOrders.button.text = getString(R.string.orders_history_empty_button)
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            OrdersNews.ShowErrorToast -> {
                Toast.makeText(context, CoreR.string.error_toast, Toast.LENGTH_SHORT).show()
            }

            OrdersNews.OpenPreviousPage -> {
                findNavController().popBackStack()
            }

            is OrdersNews.OpenOrderDetail -> {
                val action =
                    OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(news.id)
                findNavController().navigate(action)
            }

            OrdersNews.OpenMainPage -> {
                (requireActivity() as BottomNavigatable).navigateToHome()
            }
        }
    }
}