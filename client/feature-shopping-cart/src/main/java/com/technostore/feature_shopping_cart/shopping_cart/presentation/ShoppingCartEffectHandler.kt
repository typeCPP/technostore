package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_shopping_cart.business.ShoppingCartRepository

class ShoppingCartEffectHandler(
    private val shoppingCartRepository: ShoppingCartRepository
) : EffectHandler<ShoppingCartState, ShoppingCartEvent> {
    override suspend fun process(
        event: ShoppingCartEvent,
        currentState: ShoppingCartState,
        store: Store<ShoppingCartState, ShoppingCartEvent>
    ) {
        when (event) {
            ShoppingCartUiEvent.Init -> {
                store.dispatch(ShoppingCartEvent.StartLoading)
                val result = shoppingCartRepository.getCurrentOrder()
                when (result) {
                    is Result.Success -> {
                        store.dispatch(ShoppingCartEvent.EndLoading)
                        store.dispatch(ShoppingCartEvent.OrderDetailsLoaded(result.data!!))
                    }

                    else -> {
                        store.acceptNews(ShoppingCartNews.ShowErrorToast)
                    }
                }
            }

            is ShoppingCartUiEvent.OnPlusClicked -> {
                store.dispatch(ShoppingCartEvent.UpdateCount(event.productId, event.count))
                val result = shoppingCartRepository.setProductCount(event.productId, event.count)
                if (result is Result.Error) {
                    store.acceptNews(ShoppingCartNews.ShowErrorToast)
                }
            }

            is ShoppingCartUiEvent.OnMinusClicked -> {
                store.dispatch(ShoppingCartEvent.UpdateCount(event.productId, event.count))
                val result = shoppingCartRepository.setProductCount(event.productId, event.count)
                if (result is Result.Error) {
                    store.acceptNews(ShoppingCartNews.ShowErrorToast)
                }
            }

            is ShoppingCartUiEvent.OnRemoveClicked -> {
                store.dispatch(ShoppingCartEvent.RemoveItem(event.productId))
                val result = shoppingCartRepository.setProductCount(event.productId, 0)
                if (result is Result.Error) {
                    store.acceptNews(ShoppingCartNews.ShowErrorToast)
                }
            }

            ShoppingCartUiEvent.OnSetOrdersClicked -> {
                val result =
                    shoppingCartRepository.makeOrderCompleted(currentState.orderId)
                when (result) {
                    is Result.Success -> {
                        store.dispatch(ShoppingCartEvent.EndLoading)
                        store.dispatch(ShoppingCartEvent.OrderHasBeenPlaced)
                        store.acceptNews(ShoppingCartNews.ShowSuccessToast)
                    }

                    else -> {
                        store.acceptNews(ShoppingCartNews.ShowErrorToast)
                    }
                }
            }

            ShoppingCartUiEvent.OnStartShoppingClicked -> {
                store.acceptNews(ShoppingCartNews.OpenMainPage)
            }

            else -> {}
        }
    }

}