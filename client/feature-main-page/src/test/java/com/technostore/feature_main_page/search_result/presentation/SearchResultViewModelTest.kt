package com.technostore.feature_main_page.search_result.presentation

import com.technostore.common_test.TestData
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
class SearchResultViewModelTest : SearchResultBaseTest() {
    private val viewModel = SearchResultViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → isPopularity = false → send Init event`() = runTest {
        viewModel.init(isPopularity = false, categoryId = TestData.FIRST_CATEGORY_ID)
        advanceUntilIdle()
        val expectedEvent = SearchResultUiEvent.Init(
            isPopularity = false,
            categoryId = TestData.FIRST_CATEGORY_ID
        )
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `init → isPopularity = true → send Init event`() = runTest {
        viewModel.init(isPopularity = true, categoryId = TestData.FIRST_CATEGORY_ID)
        advanceUntilIdle()
        val expectedEvent = SearchResultUiEvent.Init(
            isPopularity = true,
            categoryId = TestData.FIRST_CATEGORY_ID
        )
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `plus clicked → send OnPlusClicked event`() = runTest {
        viewModel.plusClicked(firstProductModel)
        val expectedEvent = SearchResultUiEvent.OnPlusClicked(
            productId = firstProductModel.id,
            count = firstProductModel.inCartCount + 1
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `product clicked → send OnProductClicked event`() = runTest {
        viewModel.onProductClicked(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(SearchResultUiEvent.OnProductClicked(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `back button clicked → send OnBackClicked event`() = runTest {
        viewModel.onBackButtonClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(SearchResultUiEvent.OnBackClicked) }
    }
}