package com.technostore.shared_search.filter.presentation

import com.technostore.arch.result.Result
import com.technostore.shared_search.business.SharedSearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class FilterEffectHandlerTest : FilterBaseTest() {
    private val sharedSearchRepository = mockk<SharedSearchRepository> {
        coEvery { getSelectedCategories() } returns selectedCategories
        coEvery { getIsSortByPopularity() } returns false
        coEvery { getIsSortByRating() } returns false
        coEvery { getMinPrice() } returns defaultMinPrice
        coEvery { getMaxPrice() } returns defaultMaxPrice
        coEvery { getMinRating() } returns defaultMinRating
        coEvery { getMaxRating() } returns defaultMaxRating
        coEvery { getCategories() } returns Result.Success(categoryWithCheckList)
        coEvery { updateSelectedCategories(any()) } just runs
        coEvery { updateRatingBoundaries(any(), any()) } just runs
        coEvery { updateCostBoundaries(any(), any()) } just runs
        coEvery { setIsSelectByPopularity() } returns true
        coEvery { setIsSelectByRating() } returns true
    }
    private val filterEffectHandler = FilterEffectHandler(
        sharedSearchRepository = sharedSearchRepository
    )

    /* Init */
    @Test
    fun `event Init → start loading, get categories return success, stop loading, repository have selected category → set data`() =
        runTest {
            val event = FilterUiEvent.Init
            val expectedCategories =
                listOf(firstCategoryWithCheck.copy(isSelected = true), secondCategoryWithCheck)
            val expectedEvent = FilterEvent.DataLoaded(
                categories = expectedCategories,
                isSortingByPopularity = false,
                isSortingByRating = false,
                minPrice = defaultMinPrice,
                maxPrice = defaultMaxPrice,
                minRating = defaultMinRating,
                maxRating = defaultMaxRating,
            )
            filterEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(FilterEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepository.getCategories() }
            coVerify(exactly = 1) { store.dispatch(FilterEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event Init → start loading, get categories return success, stop loading, repository does not have seleced category → set data`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { getSelectedCategories() } returns arrayListOf()
            }
            val event = FilterUiEvent.Init
            val expectedEvent = FilterEvent.DataLoaded(
                categories = categoryWithCheckList,
                isSortingByPopularity = false,
                isSortingByRating = false,
                minPrice = defaultMinPrice,
                maxPrice = defaultMaxPrice,
                minRating = defaultMinRating,
                maxRating = defaultMaxRating,
            )
            filterEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(FilterEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepository.getCategories() }
            coVerify(exactly = 1) { store.dispatch(FilterEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event Init → start loading, get categories return error → show error`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { getCategories() } returns Result.Error()
            }
            val event = FilterUiEvent.Init
            filterEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(FilterEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepository.getCategories() }
            coVerify(exactly = 1) { store.acceptNews(FilterNews.ShowErrorToast) }
        }

    /* OnClickCategory */
    @Test
    fun `event OnClickCategory → update selected categories`() = runTest {
        val event = FilterUiEvent.OnClickCategory(firstCategoryWithCheck)
        filterEffectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) {
            sharedSearchRepository.updateSelectedCategories(
                firstCategoryWithCheck
            )
        }
    }

    /* OnChangedRatingBoundaries */
    @Test
    fun `event OnChangedRatingBoundaries → update rating`() = runTest {
        val event = FilterUiEvent.OnChangedRatingBoundaries(
            minValue = defaultMinRating + 1,
            maxValue = defaultMaxRating - 1
        )
        filterEffectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) {
            sharedSearchRepository.updateRatingBoundaries(
                minRating = defaultMinRating + 1,
                maxRating = defaultMaxRating - 1
            )
        }
    }

    /* OnChangedCostBoundaries */
    @Test
    fun `event OnChangedCostBoundaries → update cost`() = runTest {
        val event = FilterUiEvent.OnChangedCostBoundaries(
            minValue = defaultMinPrice + 1,
            maxValue = defaultMaxPrice - 1
        )
        filterEffectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) {
            sharedSearchRepository.updateCostBoundaries(
                minCost = defaultMinPrice + 1,
                maxCost = defaultMaxPrice - 1
            )
        }
    }

    /* OnChangedIsSortingByPopularity */
    @Test
    fun `event OnChangedIsSortingByPopularity → sort is selected → change is selected by rating and popularity`() =
        runTest {
            val event = FilterUiEvent.OnChangedIsSortingByPopularity
            filterEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByRatingBackground(
                        false
                    )
                )
            }
            coVerify(exactly = 1) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByPopularityBackground(
                        true
                    )
                )
            }
        }

    @Test
    fun `event OnChangedIsSortingByPopularity → sort is not selected → change is selected by popularity`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { setIsSelectByPopularity() } returns false
            }
            val event = FilterUiEvent.OnChangedIsSortingByPopularity
            filterEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 0) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByRatingBackground(
                        false
                    )
                )
            }
            coVerify(exactly = 1) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByPopularityBackground(
                        false
                    )
                )
            }
        }

    /* OnChangedIsSortingByRating */
    @Test
    fun `event OnChangedIsSortingByRating → sort is selected → change is selected by rating and popularity`() =
        runTest {
            val event = FilterUiEvent.OnChangedIsSortingByRating
            filterEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByPopularityBackground(false)
                )
            }
            coVerify(exactly = 1) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByRatingBackground(
                        true
                    )
                )
            }
        }

    @Test
    fun `event OnChangedIsSortingByRating → sort is not selected → change is selected by rating`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { setIsSelectByRating() } returns false
            }
            val event = FilterUiEvent.OnChangedIsSortingByRating
            filterEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 0) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByPopularityBackground(
                        false
                    )
                )
            }
            coVerify(exactly = 1) {
                store.acceptNews(
                    FilterNews.ChangeIsSortingByRatingBackground(
                        false
                    )
                )
            }
        }

    /* OnNextButtonClicked */
    @Test
    fun `event OnNextButtonClicked → open prev page`() = runTest {
        val event = FilterUiEvent.OnNextButtonClicked
        filterEffectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(FilterNews.OpenPreviousPage) }
    }

    /* OnBackButtonClicked */
    @Test
    fun `event OnBackButtonClicked → open prev page`() = runTest {
        val event = FilterUiEvent.OnBackButtonClicked
        filterEffectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(FilterNews.OpenPreviousPage) }
    }
}