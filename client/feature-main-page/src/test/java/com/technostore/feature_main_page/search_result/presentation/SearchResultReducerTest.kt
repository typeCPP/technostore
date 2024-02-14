package com.technostore.feature_main_page.search_result.presentation

import com.technostore.common_test.TestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchResultReducerTest : SearchResultBaseTest() {
    private val reducer = SearchResultReducer()
    private val defaultState = SearchResultState()

    /* StartLoading */
    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = SearchResultEvent.StartLoading
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState.isLoading)
    }

    /* StartLoading */
    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = SearchResultEvent.EndLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(!newState.isLoading)
    }

    /* DataLoaded */
    @Test
    fun `event DataLoaded → set data, set isEmpty = false`() {
        val event = SearchResultEvent.DataLoaded(products = defaultProducts)
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState.products == defaultProducts)
    }

    /* UpdateCount */
    @Test
    fun `event UpdateCount → product exists → update count`() {
        val newCount = TestData.FIRST_PRODUCT_COUNT + 1
        val event = SearchResultEvent.UpdateCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = newCount
        )
        val newState = reducer.reduce(defaultState.copy(products = defaultProducts), event)
        val updatedProduct = newState.products.find { it.id == TestData.FIRST_PRODUCT_ID }
        assertTrue(updatedProduct != null && updatedProduct.inCartCount == newCount)
    }

    @Test
    fun `event UpdateCount → product does not exists → dpes not update count`() {
        val newCount = TestData.FIRST_PRODUCT_COUNT + 1
        val event = SearchResultEvent.UpdateCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = newCount
        )
        val currentState = defaultState.copy(
            products = listOf(secondProductModel)
        )
        val newState = reducer.reduce(currentState, event)
        assertEquals(currentState, newState)
    }

    /* UI event */
    @Test
    fun `UI event → return current state`() {
        val event = SearchResultUiEvent.OnBackClicked
        val newState = reducer.reduce(defaultState, event)
        assertEquals(defaultState, newState)
    }
}