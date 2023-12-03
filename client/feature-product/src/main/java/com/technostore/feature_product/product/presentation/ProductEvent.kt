package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.Event
import com.technostore.feature_product.business.model.ProductDetailModel
import com.technostore.feature_product.business.model.ReviewModel

sealed class ProductEvent : Event {
    data object StartLoading : ProductEvent()
    data object EndLoading : ProductEvent()
    data class OnDataLoaded(val productDetail: ProductDetailModel) : ProductEvent()
    data class OnReviewLoaded(val text: String?) : ProductEvent()
    data class UpdateRating(val newRating: Int, val text: String?) : ProductEvent()
    data class UpdateReviews(val reviews: List<ReviewModel>) : ProductEvent()
    data class UpdateInCartCount(val count: Int) : ProductEvent()
}

sealed class ProductUiEvent : ProductEvent() {
    data class Init(val productId: Long) : ProductUiEvent()
    data object OnRateClicked : ProductUiEvent()
    class OnBuyClicked(val productId: Long) : ProductUiEvent()
    data object OnMoreDescriptionClicked : ProductUiEvent()
    class OnMoreReviewClicked(val productId: Long) : ProductUiEvent()
    data object OnBackClicked : ProductUiEvent()

    class SetReview(val rating: Int, val text: String?) : ProductUiEvent()
}