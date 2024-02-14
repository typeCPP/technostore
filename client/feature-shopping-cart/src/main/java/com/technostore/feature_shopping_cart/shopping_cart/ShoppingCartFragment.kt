package com.technostore.feature_shopping_cart.shopping_cart

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.technostore.arch.mvi.News
import com.technostore.feature_profile.R
import com.technostore.core.R as CoreR
import com.technostore.feature_profile.databinding.ShoppingCartFragmentBinding
import com.technostore.feature_shopping_cart.business.model.ProductOrderModel
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartNews
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartState
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartViewModel
import com.technostore.feature_shopping_cart.shopping_cart.ui.ShoppingCartAdapter
import com.technostore.navigation.BottomNavigatable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingCartFragment : Fragment() {
    private val viewModel by viewModels<ShoppingCartViewModel>()
    private lateinit var binding: ShoppingCartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = ShoppingCartFragmentBinding.inflate(inflater)
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
        setOnClickListenerForSetOrderButton()
        setOnClickListenerForStartShoppingButton()
    }


    private fun setAdapterForRecyclerView() {
        val adapter = ShoppingCartAdapter(
            onClickPlus = { item -> onClickPlus(item) },
            onClickMinus = { item -> onClickMinus(item) },
            onClickRemove = { item -> onClickRemove(item) }
        )
        val layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        binding.productsList.layoutManager = layoutManager
        binding.productsList.adapter = adapter
    }

    private fun setOnClickListenerForSetOrderButton() {
        binding.setOrder.setOnClickListener {
            viewModel.setOrderClicked()
        }
    }

    private fun onClickPlus(product: ProductOrderModel) {
        viewModel.plusClicked(product)
    }

    private fun onClickMinus(product: ProductOrderModel) {
        viewModel.minusClicked(product)
    }

    private fun onClickRemove(product: ProductOrderModel) {
        viewModel.removeClicked(product.id)
    }

    private fun setOnClickListenerForStartShoppingButton() {
        val button = view?.findViewById<AppCompatTextView>(CoreR.id.button)
        button?.setOnClickListener {
            viewModel.startShopping()
        }
    }

    private fun render(state: ShoppingCartState) {
        with(binding) {
            layoutShimmer.slShimmer.isVisible = state.isLoading
            setOrder.isVisible =
                !state.isLoading && !state.products.isNullOrEmpty()
            productsList.isVisible = !state.isLoading && !state.products.isNullOrEmpty()
            emptyShoppingCart.clMain.isVisible =
                !state.isLoading && state.products.isNullOrEmpty()

            val adapter = binding.productsList.adapter as ShoppingCartAdapter
            adapter.submitList(state.products)
            renderEmptyShoppingCart()
        }
    }

    private fun renderEmptyShoppingCart() {
        with(binding) {
            emptyShoppingCart.title.text = getString(R.string.shopping_cart_empty)
            emptyShoppingCart.description.text = getString(R.string.shopping_cart_start_shopping)
            emptyShoppingCart.button.text = getString(R.string.shopping_cart_empty_button)
        }
    }

    @SuppressLint("ShowToast")
    private fun showNews(news: News) {
        when (news) {
            ShoppingCartNews.ShowErrorToast -> {
                Toast.makeText(
                    context,
                    CoreR.string.error_toast,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ShoppingCartNews.ShowSuccessToast -> {
                Toast.makeText(
                    context,
                    R.string.shopping_cart_order_has_been_placed,
                    Toast.LENGTH_SHORT
                ).show()
            }

            ShoppingCartNews.OpenMainPage -> {
                (activity as BottomNavigatable).navigateToHome()
            }
        }
    }
}