package com.technostore.shared_search.filter.presentation

import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FilterViewModelTest : FilterBaseTest() {
    private val viewModel = FilterViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        coVerify { store.dispatch(FilterUiEvent.Init) }
    }

    @Test
    fun `next clicked → send OnNextButtonClicked event`() = runTest {
        viewModel.onNextButtonClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(FilterUiEvent.OnNextButtonClicked) }
    }

    @Test
    fun `back clicked → send OnBackButtonClicked event`() = runTest {
        viewModel.onBackClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(FilterUiEvent.OnBackButtonClicked) }
    }

    @Test
    fun `category clicked → send OnClickCategory event`() = runTest {
        viewModel.onClickCategory(firstCategoryWithCheck)
        advanceUntilIdle()
        coVerify { store.dispatch(FilterUiEvent.OnClickCategory(firstCategoryWithCheck)) }
    }

    @Test
    fun `change rating boundaries → send OnChangedRatingBoundaries event`() = runTest {
        viewModel.changeRatingBoundaries(minValue = defaultMinRating, maxValue = defaultMaxRating)
        val expectedEvent = FilterUiEvent.OnChangedRatingBoundaries(
            minValue = defaultMinRating,
            maxValue = defaultMaxRating
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `change cost boundaries → send OnChangedCostBoundaries event`() = runTest {
        viewModel.changeCostBoundaries(minValue = defaultMinPrice, maxValue = defaultMaxPrice)
        val expectedEvent = FilterUiEvent.OnChangedCostBoundaries(
            minValue = defaultMinPrice,
            maxValue = defaultMaxPrice
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `change is sorting by popularity → send OnChangedIsSortingByPopularity event`() = runTest {
        viewModel.changeIsSortingByPopularity()
        advanceUntilIdle()
        coVerify { store.dispatch(FilterUiEvent.OnChangedIsSortingByPopularity) }
    }

    @Test
    fun `change is sorting by rating → send OnChangedIsSortingByRating event`() = runTest {
        viewModel.changeIsSortingByRating()
        advanceUntilIdle()
        coVerify { store.dispatch(FilterUiEvent.OnChangedIsSortingByRating) }
    }
}