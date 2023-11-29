package com.technostore.feature_profile.orders.presentation

import com.technostore.arch.mvi.Event
import com.technostore.network.model.order.response.Order

sealed class OrdersEvent : Event {
    data object StartLoading : OrdersEvent()
    data object EndLoading : OrdersEvent()
    data class OrdersLoaded(val orders: List<Order>) : OrdersEvent()
}

sealed class OrdersUiEvent : OrdersEvent() {
    data object Init : OrdersUiEvent()
    data object OnBackClicked : OrdersUiEvent()
    data class OnOrderClicked(val id: Long) : OrdersUiEvent()
}