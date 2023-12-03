package com.technostore.feature_order.order_detail.presentation

import com.technostore.arch.mvi.Reducer

class OrderDetailReducer : Reducer<OrderDetailState, OrderDetailEvent> {
    override fun reduce(currentState: OrderDetailState, event: OrderDetailEvent): OrderDetailState {
        return when (event) {
            OrderDetailEvent.StartLoading -> {
                currentState.copy(isLoading = true)
            }

            OrderDetailEvent.EndLoading -> {
                currentState.copy(isLoading = false)
            }

            is OrderDetailEvent.OrderDetailsLoaded -> {
                currentState.copy(orderDetail = event.order)
            }

            else -> currentState
        }
    }
}