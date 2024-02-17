package com.technostore.feature_order.orders.presentation

import com.technostore.network.model.order.response.Order
import org.junit.Assert
import org.junit.Test

class OrdersReducerTest {

    private val ordersReducer = OrdersReducer()
    private val defaultState = OrdersState()

    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = OrdersEvent.StartLoading
        val newState = ordersReducer.reduce(defaultState.copy(isLoading = false), event)
        Assert.assertTrue(newState.isLoading)
    }

    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = OrdersEvent.EndLoading
        val newState = ordersReducer.reduce(defaultState.copy(isLoading = true), event)
        Assert.assertTrue(!newState.isLoading)
    }

    @Test
    fun `event OrdersLoaded → set ordersList = ids`() {
        val event = OrdersEvent.OrdersLoaded(Order(listOf(1, 2, 3)))
        val newState = ordersReducer.reduce(defaultState.copy(ordersList = listOf(1)), event)
        Assert.assertTrue(newState.ordersList == listOf<Long>(1, 2, 3))
    }

    @Test
    fun ` UI event → do nothing`() {
        val event = OrdersUiEvent.OnBackClicked
        val newState = ordersReducer.reduce(defaultState, event)
        Assert.assertTrue(newState == defaultState)
    }
}