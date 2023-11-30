package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_shopping_cart.business.model.ProductOrderModel

data class ShoppingCartState(
    val isLoading: Boolean = false,
    val products: List<ProductOrderModel> = emptyList(),
    val orderId: Long = 0
) : State