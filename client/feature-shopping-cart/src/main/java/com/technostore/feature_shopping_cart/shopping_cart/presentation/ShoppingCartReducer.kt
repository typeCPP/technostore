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
                currentState.copy(products = event.order.products, orderId = event.order.id)
            }

            ShoppingCartEvent.OrderHasBeenPlaced -> {
                currentState.copy(products = emptyList(), orderId = 0)
            }

            is ShoppingCartEvent.UpdateCount -> {
                val product = currentState.products?.find { it.id == event.productId }
                if (product != null) {
                    val newProducts = currentState.products.map { model ->
                        if (model == product) product.count = event.count
                        product
                    }
                    currentState.copy(products = newProducts)
                } else {
                    currentState
                }
            }

            is ShoppingCartEvent.RemoveItem -> {
                val newProducts = currentState.products?.filter { it.id != event.productId }
                currentState.copy(products = newProducts)
            }

            else -> currentState
        }
    }

}