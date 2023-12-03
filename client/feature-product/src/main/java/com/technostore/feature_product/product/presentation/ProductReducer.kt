package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.Reducer

class ProductReducer : Reducer<ProductState, ProductEvent> {
    override fun reduce(currentState: ProductState, event: ProductEvent): ProductState {
        return when (event) {
            ProductEvent.StartLoading -> currentState.copy(isLoading = true)
            ProductEvent.EndLoading -> currentState.copy(isLoading = false)
            is ProductEvent.OnDataLoaded -> {
                currentState.copy(
                    productDetail = event.productDetail,
                    userRating = event.productDetail.userRating
                )
            }

            is ProductEvent.UpdateRating -> {
                val productDetail = currentState.productDetail!!
                productDetail.userRating = event.newRating
                currentState.copy(
                    productDetail = productDetail,
                    userRating = event.newRating,
                    userReviewText = event.text
                )
            }

            is ProductEvent.UpdateInCartCount -> {
                val productDetail = currentState.productDetail!!
                productDetail.inCartCount = event.count
                currentState.copy(productDetail = productDetail)
            }

            is ProductEvent.OnReviewLoaded -> {
                currentState.copy(userReviewText = event.text)
            }

            is ProductEvent.UpdateReviews -> {
                val productDetail = currentState.productDetail!!
                productDetail.reviews = event.reviews
                currentState.copy(productDetail = productDetail)
            }

            else -> currentState
        }
    }
}