package com.technostore.feature_order.order_detail.presentation

import com.technostore.arch.mvi.Event
import com.technostore.feature_order.business.model.OrderDetailModel

sealed class OrderDetailEvent : Event {
    data object StartLoading : OrderDetailEvent()
    data object EndLoading : OrderDetailEvent()
    data class OrderDetailsLoaded(val order: OrderDetailModel) : OrderDetailEvent()
}

sealed class OrderDetailUiEvent : OrderDetailEvent() {
    data class Init(val id: Long) : OrderDetailUiEvent()
    data class OnProductClicked(val productId: Long) : OrderDetailUiEvent()
    data object OnBackClicked : OrderDetailUiEvent()

}