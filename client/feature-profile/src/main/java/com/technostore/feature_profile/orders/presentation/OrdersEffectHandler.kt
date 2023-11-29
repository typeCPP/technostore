package com.technostore.feature_profile.orders.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.ProfileRepository

class OrdersEffectHandler(
    private val profileRepository: ProfileRepository
) : EffectHandler<OrdersState, OrdersEvent> {
    override suspend fun process(
        event: OrdersEvent,
        currentState: OrdersState,
        store: Store<OrdersState, OrdersEvent>
    ) {
        when (event) {
            OrdersUiEvent.Init -> {
                store.dispatch(OrdersEvent.StartLoading)
                val result = profileRepository.getCompletedOrders()
                when (result) {
                    is Result.Success -> {
                        store.dispatch(OrdersEvent.OrdersLoaded(result.data!!))
                        store.dispatch(OrdersEvent.EndLoading)
                    }

                    is Result.Error -> {
                        store.acceptNews(OrdersNews.ShowErrorToast)
                    }
                }
            }

            OrdersUiEvent.OnBackClicked -> {
                store.acceptNews(OrdersNews.OpenPreviousPage)
            }

            is OrdersUiEvent.OnOrderClicked -> {
                store.acceptNews(OrdersNews.OpenOrderDetail(event.id))
            }

            else -> {}
        }
    }
}