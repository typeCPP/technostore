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
                currentState.copy(productDetail = productDetail, userRating = event.newRating)
            }

            else -> currentState
        }
    }
}