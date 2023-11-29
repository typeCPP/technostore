package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.Reducer
import com.technostore.arch.mvi.State

class ProductDescriptionReducer : Reducer<State, ProductDescriptionUiEvent> {
    override fun reduce(currentState: State, event: ProductDescriptionUiEvent): State {
        return currentState
    }
}
