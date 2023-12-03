package com.technostore.feature_order.orders.presentation

import com.technostore.arch.mvi.State

data class OrdersState(
    val isLoading: Boolean = false,
    val ordersList: List<Long> = emptyList()
) : State