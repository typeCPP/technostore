package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.Event
import com.technostore.feature_product.business.model.ProductDetailModel

sealed class ProductEvent : Event {
    data object StartLoading : ProductEvent()
    data object EndLoading : ProductEvent()
    class OnDataLoaded(val productDetail: ProductDetailModel) : ProductEvent()
}

sealed class ProductUiEvent : ProductEvent() {
    data class Init(val productId: Long) : ProductUiEvent()
    data object OnRateClicked : ProductUiEvent()
    class OnBuyClicked(val productId: Long) : ProductUiEvent()
    data object OnMoreDescriptionClicked : ProductUiEvent()
    class OnMoreReviewClicked(val productId: Long) : ProductUiEvent()
    data object OnBackClicked : ProductUiEvent()
}