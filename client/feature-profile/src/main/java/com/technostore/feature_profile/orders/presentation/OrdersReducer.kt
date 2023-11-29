package com.technostore.feature_profile.orders.presentation

import com.technostore.arch.mvi.Reducer

class OrdersReducer : Reducer<OrdersState, OrdersEvent> {
    override fun reduce(currentState: OrdersState, event: OrdersEvent): OrdersState {
        return when (event) {
            OrdersEvent.StartLoading -> {
                currentState.copy(isLoading = true)
            }

            OrdersEvent.EndLoading -> {
                currentState.copy(isLoading = false)
            }

            is OrdersEvent.OrdersLoaded -> {
                currentState.copy(ordersList = event.orders)
            }

            else -> currentState
        }
    }
}