package com.technostore.feature_search.search.presentation

import com.technostore.common_test.TestData
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchReducerTest : SearchBaseTest() {
    private val reducer = SearchReducer()

    /* StartLoading */
    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = SearchEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = SearchEvent.EndLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = true), event)
        assertTrue(!newState.isLoading)
    }

    /* DataLoaded */
    @Test
    fun `event DataLoaded → update data`() {
        val event = SearchEvent.DataLoaded(products = products)
        val currentState = defaultState.copy(isEmpty = true, products = emptyList())
        val newState = reducer.reduce(currentState, event)
        assertTrue(!newState.isEmpty && newState.products == products)
    }

    /* UpdateCount */
    @Test
    fun `event UpdateCount → product exists → update product count`() {
        val newCount = TestData.FIRST_PRODUCT_COUNT + 1
        val event = SearchEvent.UpdateCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = newCount
        )
        val currentState = defaultState.copy(isEmpty = true, products = products)
        val newState = reducer.reduce(currentState, event)
        val productUpdating = newState.products.find { it.id == TestData.FIRST_PRODUCT_ID }
        assertTrue(productUpdating != null && productUpdating.inCartCount == newCount)
    }

    @Test
    fun `event UpdateCount → product does not exists → dp nothing`() {
        val newCount = TestData.FIRST_PRODUCT_COUNT + 1
        val event = SearchEvent.UpdateCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = newCount
        )
        val currentState = defaultState.copy(isEmpty = false, products = listOf(secondProductModel))
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState == currentState)
    }

    /* IsEmpty */
    @Test
    fun `event IsEmpty → update products`() {
        val event = SearchEvent.IsEmpty
        val currentState = defaultState.copy(isEmpty = false, products = products)
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.isEmpty && !newState.isLoading && newState.products.isEmpty())
    }

    /* ClearData */
    @Test
    fun `event IsEmpty → clear products`() {
        val event = SearchEvent.ClearData
        val currentState = defaultState.copy(isEmpty = false, products = products)
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.products.isEmpty())
    }

    /* ui event */
    @Test
    fun ` UI event → do nothing`() {
        val event = SearchUiEvent.OnFilterClicked
        val currentState = defaultState.copy(isEmpty = false, products = products)
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState == currentState)
    }
}