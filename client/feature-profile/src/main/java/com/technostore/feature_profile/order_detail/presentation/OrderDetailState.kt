package com.technostore.feature_profile.order_detail.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_profile.business.model.OrderDetailModel

data class OrderDetailState(
    val isLoading: Boolean = false,
    val orderDetail: OrderDetailModel? = null
) : State