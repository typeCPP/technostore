package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.arch.mvi.Event
import com.technostore.feature_shopping_cart.business.model.OrderDetailModel

sealed class ShoppingCartEvent : Event {
    data object StartLoading : ShoppingCartEvent()
    data object EndLoading : ShoppingCartEvent()
    data class OrderDetailsLoaded(val order: OrderDetailModel) : ShoppingCartEvent()
    data object OrderHasBeenPlaced : ShoppingCartEvent()
    data class UpdateCount(val productId: Long, val count: Int) : ShoppingCartEvent()
    data class RemoveItem(val productId: Long) : ShoppingCartEvent()

}

sealed class ShoppingCartUiEvent : ShoppingCartEvent() {
    data object Init : ShoppingCartUiEvent()
    data class OnPlusClicked(val productId: Long, val count: Int) : ShoppingCartUiEvent()
    data class OnMinusClicked(val productId: Long, val count: Int) : ShoppingCartUiEvent()
    data class OnRemoveClicked(val productId: Long) : ShoppingCartUiEvent()
    data object OnSetOrdersClicked : ShoppingCartUiEvent()
    data object OnStartShoppingClicked : ShoppingCartUiEvent()
}