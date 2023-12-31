package com.technostore.feature_order.orders.presentation

import com.technostore.arch.mvi.Event
import com.technostore.network.model.order.response.Order

sealed class OrdersEvent : Event {
    data object StartLoading : OrdersEvent()
    data object EndLoading : OrdersEvent()
    data class OrdersLoaded(val orders: Order) : OrdersEvent()
}

sealed class OrdersUiEvent : OrdersEvent() {
    data object Init : OrdersUiEvent()
    data object OnBackClicked : OrdersUiEvent()
    data class OnOrderClicked(val id: Long) : OrdersUiEvent()
    data object OnStartShoppingClicked : OrdersUiEvent()
}