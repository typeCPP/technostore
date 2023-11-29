package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.Event

sealed class ProductDescriptionUiEvent : Event {
    data object OnBackClicked : ProductDescriptionUiEvent()
}
