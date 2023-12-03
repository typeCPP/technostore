package com.technostore.feature_profile.orders.presentation

import com.technostore.arch.mvi.State
import com.technostore.network.model.order.response.Order

data class OrdersState(
    val isLoading: Boolean = false,
    val ordersList: List<Long> = emptyList()
) : State