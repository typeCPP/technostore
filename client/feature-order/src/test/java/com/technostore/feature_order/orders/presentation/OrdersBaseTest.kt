package com.technostore.feature_order.orders.presentation

import com.technostore.arch.mvi.Store
import io.mockk.mockk

open class OrdersBaseTest {
    protected val store = mockk<Store<OrdersState, OrdersEvent>>(relaxed = true)
}