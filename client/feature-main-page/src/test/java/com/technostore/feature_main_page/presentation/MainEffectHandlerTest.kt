package com.technostore.feature_main_page.presentation

import com.technostore.arch.mvi.Store
import com.technostore.feature_main_page.main_page.presentation.MainEvent
import com.technostore.feature_main_page.main_page.presentation.MainState
import com.technostore.feature_main_page.main_page.presentation.MainUiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_main_page.main_page.presentation.MainNews
import com.technostore.shared_search.business.error.SearchEmpty

@ExperimentalCoroutinesApi
class MainEffectHandlerTest : MainPageBaseTest() {

    private val store = mockk<Store<MainState, MainEvent>>(relaxed = true)
    private val word = "word"

    /* Init */
    @Test
    fun `event init → call clear page, start loading, search by popularity return success, categories return success  → stop loading, set data`() =
        testScope.runTest {
            val event = MainUiEvent.Init
            effectHandler.process(event, defaultState, store)
            val expectedEvent = MainEvent.MainDataLoaded(
                popularProducts = defaultProducts,
                categories = defaultCategories
            )
            coVerify(exactly = 1) { sharedSearchRepositoryMock.clearNumberPage() }
            coVerify(exactly = 1) { store.dispatch(MainEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByPopularity() }
            coVerify(exactly = 1) { sharedSearchRepositoryMock.getCategories() }
            coVerify(exactly = 1) { store.dispatch(MainEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event init → call clear page, start loading, search by popularity return error → get categories did not call, show error toast`() =
        testScope.runTest {
            mainRepositoryMock.apply {
                coEvery { searchByPopularity() } returns Result.Error()
            }
            val event = MainUiEvent.Init

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepositoryMock.clearNumberPage() }
            coVerify(exactly = 1) { store.dispatch(MainEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByPopularity() }
            coVerify(exactly = 0) { sharedSearchRepositoryMock.getCategories() }
            coVerify(exactly = 1) { store.acceptNews(MainNews.ShowErrorToast) }
        }

    @Test
    fun `event init → call clear page, start loading, search by popularity return success, get categories return error → show error toast`() =
        testScope.runTest {
            sharedSearchRepositoryMock.apply {
                coEvery { getCategories() } returns Result.Error()
            }
            val event = MainUiEvent.Init

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepositoryMock.clearNumberPage() }
            coVerify(exactly = 1) { store.dispatch(MainEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByPopularity() }
            coVerify(exactly = 1) { sharedSearchRepositoryMock.getCategories() }
            coVerify(exactly = 1) { store.acceptNews(MainNews.ShowErrorToast) }
        }

    /* OnTextChanged */
    @Test
    fun `event onTextChanged → start loading, search products return success with not empty product list → stop loading, set data`() =
        testScope.runTest {
            val event = MainUiEvent.OnTextChanged(word)
            val expectedEvent = MainEvent.DataLoaded(products = defaultProducts)

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(MainEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepositoryMock.searchProducts(word) }
            coVerify(exactly = 1) { store.dispatch(MainEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event onTextChanged → start loading, search products return empty error → dispatch empty event`() =
        testScope.runTest {
            sharedSearchRepositoryMock.apply {
                coEvery { searchProducts(any()) } returns Result.Error(SearchEmpty())
            }
            val event = MainUiEvent.OnTextChanged(word)

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(MainEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepositoryMock.searchProducts(word) }
            coVerify(exactly = 1) { store.dispatch(MainEvent.IsEmpty) }
        }

    @Test
    fun `event onTextChanged → start loading, search products return error without body → show error toast`() =
        testScope.runTest {
            sharedSearchRepositoryMock.apply {
                coEvery { searchProducts(any()) } returns Result.Error()
            }
            val event = MainUiEvent.OnTextChanged(word)

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(MainEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepositoryMock.searchProducts(word) }
            coVerify(exactly = 1) { store.acceptNews(MainNews.ShowErrorToast) }
        }

    /* LoadMoreProducts */

    @Test
    fun `event LoadMoreProducts → search products return success → set data`() =
        testScope.runTest {
            val event = MainUiEvent.LoadMoreProducts(word)
            val expectedEvent = MainEvent.DataLoaded(products = defaultProducts)

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepositoryMock.searchProducts(word) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event LoadMoreProducts → search products return search empty → does not show error toast`() =
        testScope.runTest {
            sharedSearchRepositoryMock.apply {
                coEvery { searchProducts(any()) } returns Result.Error(SearchEmpty())
            }
            val event = MainUiEvent.LoadMoreProducts(word)

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepositoryMock.searchProducts(word) }
            coVerify(exactly = 0) { store.acceptNews(MainNews.ShowErrorToast) }
        }

    @Test
    fun `event LoadMoreProducts → search products return search error → show error toast`() =
        testScope.runTest {
            sharedSearchRepositoryMock.apply {
                coEvery { searchProducts(any()) } returns Result.Error()
            }
            val event = MainUiEvent.LoadMoreProducts(word)

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepositoryMock.searchProducts(word) }
            coVerify(exactly = 1) { store.acceptNews(MainNews.ShowErrorToast) }
        }

    /* OnBackClicked */
    @Test
    fun `event OnBackClicked → navigate back, clear data`() = testScope.runTest {
        val event = MainUiEvent.OnBackClicked

        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(MainNews.NavigateBack) }
        coVerify(exactly = 1) { store.dispatch(MainEvent.ClearData) }
    }

    /* OnProductClicked */
    @Test
    fun `event OnProductClicked → open product page`() = testScope.runTest {
        val event = MainUiEvent.OnProductClicked(productId = TestData.FIRST_PRODUCT_ID)

        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(MainNews.OpenProductPage(TestData.FIRST_PRODUCT_ID)) }
    }

    /* OnPlusClicked */
    @Test
    fun `event OnPlusClicked → setProductCount return success → update count`() =
        testScope.runTest {
            val newCount = TestData.FIRST_PRODUCT_COUNT + 1
            val event = MainUiEvent.OnPlusClicked(
                productId = TestData.FIRST_PRODUCT_ID,
                count = newCount
            )

            effectHandler.process(event, defaultState, store)
            val expectedEvent = MainEvent.UpdateCount(
                TestData.FIRST_PRODUCT_ID,
                newCount
            )
            coVerify(exactly = 1) {
                sharedSearchRepositoryMock.setProductCount(
                    TestData.FIRST_PRODUCT_ID,
                    newCount
                )
            }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    /* OnPlusClicked */
    @Test
    fun `event OnPlusClicked → setProductCount return error → show error toast`() =
        testScope.runTest {
            sharedSearchRepositoryMock.apply {
                coEvery { setProductCount(any(), any()) } returns Result.Error()
            }
            val newCount = TestData.FIRST_PRODUCT_COUNT + 1
            val event = MainUiEvent.OnPlusClicked(
                productId = TestData.FIRST_PRODUCT_ID,
                count = newCount
            )

            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                sharedSearchRepositoryMock.setProductCount(
                    TestData.FIRST_PRODUCT_ID,
                    newCount
                )
            }
            coVerify(exactly = 1) { store.acceptNews(MainNews.ShowErrorToast) }
        }

    /* OnFilterClicked */
    @Test
    fun `event OnFilterClicked → open filter`() = testScope.runTest {
        val event = MainUiEvent.OnFilterClicked
        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { store.acceptNews(MainNews.OpenFilter) }
    }

    /* OnCategoryClicked */
    @Test
    fun `event OnCategoryClicked → open result`() = testScope.runTest {
        val event = MainUiEvent.OnCategoryClicked(TestData.FIRST_CATEGORY_ID)
        effectHandler.process(event, defaultState, store)

        val expectedNews = MainNews.OpenResultByCategory(TestData.FIRST_CATEGORY_ID)
        coVerify(exactly = 1) { store.acceptNews(expectedNews) }
    }

    /* OnSearchCLicked */
    @Test
    fun `event OnSearchCLicked → clear is main page`() = testScope.runTest {
        val event = MainUiEvent.OnSearchCLicked
        effectHandler.process(event, defaultState, store)

        val expectedEvent = MainEvent.SetIsMainPage(false)
        coVerify(exactly = 1) { store.dispatch(expectedEvent) }
    }

    /* OnTextIsEmpty */
    @Test
    fun `event OnTextIsEmpty → set is main page`() = testScope.runTest {
        val event = MainUiEvent.OnTextIsEmpty
        effectHandler.process(event, defaultState, store)

        val expectedEvent = MainEvent.SetIsMainPage(true)
        coVerify(exactly = 1) { store.dispatch(expectedEvent) }
    }

    /* MorePopularClicked */
    @Test
    fun `event MorePopularClicked → open search result by popularity`() = testScope.runTest {
        val event = MainUiEvent.MorePopularClicked
        effectHandler.process(event, defaultState, store)

        val expectedNews = MainNews.OpenResultByPopularity
        coVerify(exactly = 1) { store.acceptNews(expectedNews) }
    }
}