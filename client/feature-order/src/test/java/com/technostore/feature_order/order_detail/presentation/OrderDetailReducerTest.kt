package com.technostore.feature_order.order_detail.presentation

import com.technostore.common_test.TestData
import com.technostore.feature_order.business.model.OrderDetailModel
import com.technostore.feature_order.business.model.ProductOrderModel
import com.technostore.network.utils.URL
import org.junit.Assert.assertTrue
import org.junit.Test


class OrderDetailReducerTest : OrderDetailBaseTest() {
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
        val products = listOf(
            ProductOrderModel(
                id = TestData.PRODUCT_ID,
                photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.PRODUCT_PHOTO_LINK}",
                name = TestData.PRODUCT_NAME,
                rating = TestData.PRODUCT_RATING,
                count = TestData.PRODUCT_COUNT,
                price = TestData.PRODUCT_PRICE
            )
        )
        val order = OrderDetailModel(
            id = TestData.ORDER_ID,
            products = products
        )
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