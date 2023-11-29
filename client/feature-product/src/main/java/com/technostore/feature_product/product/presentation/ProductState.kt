package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.State
import com.technostore.network.model.product.response.ProductDetail

data class ProductState(
    val isLoading: Boolean = false,
    val productDetail: ProductDetail? = null
) : State