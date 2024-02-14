package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.Reducer

class ProductDescriptionReducer : Reducer<ProductDescriptionState, ProductDescriptionUiEvent> {
    override fun reduce(
        currentState: ProductDescriptionState,
        event: ProductDescriptionUiEvent
    ): ProductDescriptionState {
        return currentState
    }
}
