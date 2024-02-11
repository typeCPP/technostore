package com.technostore.feature_search.search.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.TestData
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest : SearchBaseTest() {
    private val store = mockk<Store<SearchState, SearchEvent>>(relaxed = true)
    private val viewModel = SearchViewModel(store)
    private val text = "Комп"
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        coVerify { store.dispatch(SearchUiEvent.Init) }
    }

    @Test
    fun `search → send OnTextChanged event`() = runTest {
        viewModel.search(text)
        advanceUntilIdle()
        coVerify { store.dispatch(SearchUiEvent.OnTextChanged(text)) }
    }

    @Test
    fun `plus clicked → send OnPlusClicked event`() = runTest {
        viewModel.plusClicked(secondProductModel)
        val expectedEvent = SearchUiEvent.OnPlusClicked(
            productId = secondProductModel.id,
            count = secondProductModel.inCartCount + 1
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `filter clicked → send OnFilterClicked event`() = runTest {
        viewModel.onFilterClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(SearchUiEvent.OnFilterClicked) }
    }

    @Test
    fun `product clicked → send OnProductClicked event`() = runTest {
        viewModel.onProductClicked(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(SearchUiEvent.OnProductClicked(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `load more products → send LoadMoreProducts event`() = runTest {
        viewModel.loadMoreProducts(text)
        advanceUntilIdle()
        coVerify { store.dispatch(SearchUiEvent.LoadMoreProducts(text)) }
    }

    @Test
    fun `back clicked → send OnBackClicked event`() = runTest {
        viewModel.onBackButtonClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(SearchUiEvent.OnBackClicked) }
    }
}