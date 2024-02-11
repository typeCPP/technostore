package com.technostore.feature_main_page.presentation

import com.technostore.common_test.TestData
import com.technostore.feature_main_page.main_page.presentation.MainEvent
import com.technostore.feature_main_page.main_page.presentation.MainUiEvent
import com.technostore.feature_main_page.main_page.presentation.MainViewModel
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
class MainViewModelTest : MainPageBaseTest() {
    private val viewModel = MainViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.Init) }
    }

    @Test
    fun `search → send OnTextChanged event`() = runTest {
        viewModel.search(word)
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.OnTextChanged(word)) }
    }

    @Test
    fun `plus clicked → send OnPlusClicked event`() = runTest {
        viewModel.plusClicked(firstProductModel)
        val expectedEvent = MainUiEvent.OnPlusClicked(
            productId = firstProductModel.id,
            count = firstProductModel.inCartCount + 1
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `filter clicked → send OnFilterClicked event`() = runTest {
        viewModel.onFilterClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.OnFilterClicked) }
    }

    @Test
    fun `product clicked → send OnProductClicked event`() = runTest {
        viewModel.onProductClicked(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.OnProductClicked(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `load more products → send LoadMoreProducts event`() = runTest {
        viewModel.loadMoreProducts(word)
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.LoadMoreProducts(word)) }
    }

    @Test
    fun `back clicked → send OnBackClicked event`() = runTest {
        viewModel.onBackButtonClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.OnBackClicked) }
    }

    @Test
    fun `category clicked → send OnCategoryClicked event`() = runTest {
        viewModel.onClickCategory(TestData.FIRST_CATEGORY_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.OnCategoryClicked(TestData.FIRST_CATEGORY_ID)) }
    }

    @Test
    fun `search clicked → send OnSearchCLicked event`() = runTest {
        viewModel.onClickSearch()
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.OnSearchCLicked) }
    }

    @Test
    fun `text is empty → send OnTextIsEmpty event`() = runTest {
        viewModel.textIsEmpty()
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.OnTextIsEmpty) }
    }

    @Test
    fun `more popularity clicked → send MorePopularClicked event`() = runTest {
        viewModel.morePopularityClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(MainUiEvent.MorePopularClicked) }
    }

    @Test
    fun `is main page → send SetIsMainPage = true event`() = runTest {
        viewModel.setIsMainPage(true)
        advanceUntilIdle()
        coVerify { store.dispatch(MainEvent.SetIsMainPage(true)) }
    }

    @Test
    fun `is not main page → send SetIsMainPage = false event`() = runTest {
        viewModel.setIsMainPage(false)
        advanceUntilIdle()
        coVerify { store.dispatch(MainEvent.SetIsMainPage(false)) }
    }
}