package com.technostore.feature_main_page.main_page.presentation

import com.technostore.common_test.TestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class MainReducerTest : MainPageBaseTest() {
    private val reducer = MainReducer()

    /* StartLoading */
    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = MainEvent.StartLoading
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState.isLoading)
    }

    /* StartLoading */
    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = MainEvent.EndLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(!newState.isLoading)
    }

    /* DataLoaded */
    @Test
    fun `event DataLoaded → set data, set isEmpty = false`() {
        val event = MainEvent.DataLoaded(products = defaultProducts)
        val newState = reducer.reduce(defaultState, event)
        assertTrue(!newState.isEmpty && newState.productsResult == defaultProducts)
    }

    /* MainDataLoaded */
    @Test
    fun `event MainDataLoaded → set popular products, set categories`() {
        val event = MainEvent.MainDataLoaded(
            popularProducts = defaultProducts,
            categories = defaultCategories
        )
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState.popularProducts == defaultProducts)
        assertTrue(newState.categories == defaultCategories)
    }

    /* UpdateCount */
    @Test
    fun `event UpdateCount → product exists → update count`() {
        val currentState = defaultState.copy(productsResult = defaultProducts)
        val newCount = TestData.FIRST_PRODUCT_COUNT + 1
        val event = MainEvent.UpdateCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = newCount
        )
        val newState = reducer.reduce(currentState, event)
        val changedProduct =
            newState.productsResult.find { product -> product.id == TestData.FIRST_PRODUCT_ID }
        assertTrue(changedProduct != null && changedProduct.inCartCount == newCount)
    }

    @Test
    fun `event UpdateCount → product exists → count is not updated`() {
        val currentState = defaultState.copy(productsResult = listOf(firstProductModel))
        val newCount = 10000
        val event = MainEvent.UpdateCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = newCount
        )
        val newState = reducer.reduce(currentState, event)
        assertTrue(currentState.productsResult == newState.productsResult)
    }
    /* IsEmpty */

    @Test
    fun `event IsEmpty → set isLoading = false, clear productResult, set isEmpty = true`() {
        val currentState = defaultState.copy(
            productsResult = defaultProducts,
            isLoading = true,
            isEmpty = false
        )
        val event = MainEvent.IsEmpty
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.productsResult.isEmpty() && !newState.isLoading && newState.isEmpty)
    }

    /* ClearData */
    @Test
    fun `event ClearData → set is main page, clear productResult`() {
        val currentState = defaultState.copy(
            productsResult = defaultProducts,
            isMainPage = false
        )
        val event = MainEvent.ClearData
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.productsResult.isEmpty() && newState.isMainPage)
    }

    /* SetIsMainPage */
    @Test
    fun `event SetIsMainPage, isMainPage = true → set is main page`() {
        val currentState = defaultState.copy(isMainPage = false)
        val event = MainEvent.SetIsMainPage(isMainPage = true)
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.isMainPage)
    }

    @Test
    fun `event SetIsMainPage, isMainPage = false → clear is main page`() {
        val currentState = defaultState.copy(isMainPage = true)
        val event = MainEvent.SetIsMainPage(isMainPage = false)
        val newState = reducer.reduce(currentState, event)
        assertTrue(!newState.isMainPage)
    }

    @Test
    fun `event is ui , return current state`() {
        val event = MainUiEvent.MorePopularClicked
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}