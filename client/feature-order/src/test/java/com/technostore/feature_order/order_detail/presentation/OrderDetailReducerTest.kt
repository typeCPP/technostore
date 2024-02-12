package com.technostore.feature_order.order_detail.presentation

import org.junit.Assert.assertTrue
import org.junit.Test


class OrderDetailReducerTest : OrderDetailBaseTest() {
    private val orderDetailReducer = OrderDetailReducer()
    @Test
    fun `start loading → set isLoading = true`() {
        val newState = orderDetailReducer.reduce(defaultState, OrderDetailEvent.StartLoading)
        assertTrue(newState.isLoading && newState.orderDetail == defaultState.orderDetail)
    }

    @Test
    fun `end loading → set isLoading = false`() {
        val currentState = OrderDetailState(isLoading = true)
        val newState = orderDetailReducer.reduce(currentState, OrderDetailEvent.EndLoading)
        assertTrue(!newState.isLoading && newState.orderDetail == defaultState.orderDetail)
    }

    @Test
    fun `order details loaded → set order details`() {
        val newState = orderDetailReducer.reduce(
            defaultState, OrderDetailEvent.OrderDetailsLoaded(order)
        )
        assertTrue(newState.isLoading == defaultState.isLoading && newState.orderDetail == order)
    }

    @Test
    fun `other event → return default statr`() {
        val newState = orderDetailReducer.reduce(
            defaultState, OrderDetailUiEvent.OnBackClicked
        )
        assertTrue(defaultState == newState)
    }
}