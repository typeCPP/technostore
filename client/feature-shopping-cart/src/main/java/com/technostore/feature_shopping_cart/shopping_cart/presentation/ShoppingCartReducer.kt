package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.arch.mvi.Reducer

class ShoppingCartReducer : Reducer<ShoppingCartState, ShoppingCartEvent> {
    override fun reduce(
        currentState: ShoppingCartState,
        event: ShoppingCartEvent
    ): ShoppingCartState {
        return when (event) {
            ShoppingCartEvent.StartLoading -> {
                currentState.copy(isLoading = true)
            }

            ShoppingCartEvent.EndLoading -> {
                currentState.copy(isLoading = false)
            }

            is ShoppingCartEvent.OrderDetailsLoaded -> {
                currentState.copy(products = event.order.products)
            }

            ShoppingCartEvent.OrderHasBeenPlaced -> {
                currentState.copy(products = emptyList())
            }

            is ShoppingCartEvent.UpdateCount -> {
                currentState
            }

            is ShoppingCartEvent.RemoveItem -> {
                val newProducts = currentState.products?.filter { it.id != event.productId }
                currentState.copy(products = newProducts)
            }

            else -> currentState
        }
    }

}