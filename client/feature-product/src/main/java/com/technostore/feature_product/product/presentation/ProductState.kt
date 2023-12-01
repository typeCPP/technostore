package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_product.business.model.ProductDetailModel

data class ProductState(
    val isLoading: Boolean = false,
    val productDetail: ProductDetailModel? = null,
    val userRating: Int = 0
) : State