package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.common_test.TestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShoppingCartReducerTest : ShoppingCartBaseTest() {
    /* StartLoading */
    private val reducer = ShoppingCartReducer()

    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = ShoppingCartEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = ShoppingCartEvent.EndLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = true), event)
        assertTrue(!newState.isLoading)
    }

    /* OrderDetailsLoaded */
    @Test
    fun `event OrderDetailsLoaded → update order`() {
        val event = ShoppingCartEvent.OrderDetailsLoaded(order = order)
        val newState = reducer.reduce(defaultState.copy(orderId = 0, products = emptyList()), event)
        assertTrue(newState.orderId == order.id && newState.products == order.products)
    }

    /* OrderHasBeenPlaced */
    @Test
    fun `event OrderHasBeenPlaced → clear order`() {
        val event = ShoppingCartEvent.OrderHasBeenPlaced
        val newState = reducer.reduce(
            defaultState.copy(
                orderId = TestData.ORDER_ID,
                products = listOf(firstProduct)
            ), event
        )
        val products = newState.products
        assertTrue(newState.orderId == 0L && products != null && products.isEmpty())
    }

    /* UpdateCount */
    @Test
    fun `event UpdateCount → product exists →  update product count`() {
        val event = ShoppingCartEvent.UpdateCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = TestData.FIRST_PRODUCT_COUNT + 1
        )
        val newState = reducer.reduce(
            defaultState.copy(
                orderId = TestData.ORDER_ID,
                products = listOf(firstProduct)
            ), event
        )
        val products = listOf(firstProduct.copy(count = TestData.FIRST_PRODUCT_COUNT + 1))
        assertTrue(newState.products == products)
    }

    @Test
    fun `event UpdateCount → product does not exists → return current state`() {
        val event = ShoppingCartEvent.UpdateCount(
            productId = TestData.SECOND_PRODUCT_ID,
            count = TestData.SECOND_PRODUCT_COUNT
        )
        val currentState = defaultState.copy(
            orderId = TestData.ORDER_ID,
            products = listOf(firstProduct)
        )
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState == currentState)
    }

    /* RemoveItem */
    @Test
    fun `event RemoveItem → product exists → remove product`() {
        val event = ShoppingCartEvent.RemoveItem(
            productId = TestData.SECOND_PRODUCT_ID,
        )
        val currentState = defaultState.copy(
            orderId = TestData.ORDER_ID,
            products = listOf(firstProduct, secondProduct)
        )
        val newState = reducer.reduce(currentState, event)
        val newProducts = newState.products
        assertTrue(newProducts != null && newProducts.size == 1 && newProducts[0] == firstProduct)
    }

    @Test
    fun `event RemoveItem → product does not exists → does not update products`() {
        val event = ShoppingCartEvent.RemoveItem(
            productId = TestData.SECOND_PRODUCT_ID,
        )
        val currentState = defaultState.copy(
            orderId = TestData.ORDER_ID,
            products = listOf(firstProduct)
        )
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState == currentState)
    }

    /* UI event */
    @Test
    fun `UI RemoveItem → return current state`() {
        val event = ShoppingCartUiEvent.OnStartShoppingClicked
        val newState = reducer.reduce(defaultState, event)
        assertEquals(defaultState, newState)
    }
}