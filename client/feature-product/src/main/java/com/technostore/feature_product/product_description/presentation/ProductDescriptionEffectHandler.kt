package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.State
import com.technostore.arch.mvi.Store

class ProductDescriptionEffectHandler : EffectHandler<State, ProductDescriptionUiEvent> {
    override suspend fun process(
        event: ProductDescriptionUiEvent,
        currentState: State,
        store: Store<State, ProductDescriptionUiEvent>
    ) {
        when (event) {
            ProductDescriptionUiEvent.OnBackClicked -> {
                store.acceptNews(ProductDescriptionNews.OpenPreviousPage)
            }
        }
    }
}
