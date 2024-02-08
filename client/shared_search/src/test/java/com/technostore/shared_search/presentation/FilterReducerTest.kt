package com.technostore.shared_search.presentation

import com.technostore.shared_search.filter.presentation.FilterEvent
import com.technostore.shared_search.filter.presentation.FilterReducer
import com.technostore.shared_search.filter.presentation.FilterUiEvent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterReducerTest : FilterBaseTest() {
    private val reducer = FilterReducer()

    /* StartLoading */
    @Test
    fun `event StartLoading → update is loading state`() = runTest {
        val event = FilterEvent.StartLoading
        val currentState = defaultState.copy(isLoading = false)
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → update is loading state`() = runTest {
        val event = FilterEvent.EndLoading
        val currentState = defaultState.copy(isLoading = true)
        val newState = reducer.reduce(currentState, event)
        assertTrue(!newState.isLoading)
    }

    /* DataLoaded */
    @Test
    fun `event DataLoaded → set data state`() = runTest {
        val event = FilterEvent.DataLoaded(
            categories = categoryWithCheckList,
            isSortingByPopularity = true,
            isSortingByRating = false,
            minPrice = defaultMinPrice + 1,
            maxPrice = defaultMaxPrice - 1,
            minRating = defaultMinRating + 1,
            maxRating = defaultMaxRating - 1,
        )
        val newState = reducer.reduce(defaultState, event)
        assertTrue(
            newState.categories == categoryWithCheckList &&
                    newState.isSortingByPopularity &&
                    !newState.isSortingByRating &&
                    newState.minPrice == defaultMinPrice + 1 &&
                    newState.maxPrice == defaultMaxPrice - 1 &&
                    newState.minRating == defaultMinRating + 1 &&
                    newState.maxRating == defaultMaxRating - 1
        )
    }

    /* Ui event */
    @Test
    fun `ui event → return current state`() = runTest {
        val event = FilterUiEvent.OnNextButtonClicked
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}