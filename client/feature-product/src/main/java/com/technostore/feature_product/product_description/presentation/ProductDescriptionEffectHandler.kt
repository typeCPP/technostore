package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store

class ProductDescriptionEffectHandler : EffectHandler<ProductDescriptionState, ProductDescriptionUiEvent> {
    override suspend fun process(
        event: ProductDescriptionUiEvent,
        currentState: ProductDescriptionState,
        store: Store<ProductDescriptionState, ProductDescriptionUiEvent>
    ) {
        when (event) {
            ProductDescriptionUiEvent.OnBackClicked -> {
                store.acceptNews(ProductDescriptionNews.OpenPreviousPage)
            }
        }
    }
}
