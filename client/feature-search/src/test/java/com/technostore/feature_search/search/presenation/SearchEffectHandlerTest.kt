package com.technostore.feature_search.search.presenation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_search.search.presentation.SearchEffectHandler
import com.technostore.feature_search.search.presentation.SearchEvent
import com.technostore.feature_search.search.presentation.SearchNews
import com.technostore.feature_search.search.presentation.SearchState
import com.technostore.feature_search.search.presentation.SearchUiEvent
import com.technostore.shared_search.business.SharedSearchRepository
import com.technostore.shared_search.business.error.SearchEmpty
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchEffectHandlerTest : SearchBaseTest() {
    private val store = mockk<Store<SearchState, SearchEvent>>(relaxed = true)
    private val word = "word"
    private val sharedSearchRepository = mockk<SharedSearchRepository> {
        coEvery { searchProducts(any()) } returns Result.Success(products)
        coEvery { clearNumberPage() } just runs
        coEvery { setProductCount(any(), any()) } returns Result.Success()
    }
    private val effectHandler = SearchEffectHandler(
        sharedSearchRepository = sharedSearchRepository
    )

    /* Init */
    @Test
    fun `event init → call clear page`() = runTest {
        val event = SearchUiEvent.Init
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { sharedSearchRepository.clearNumberPage() }
    }

    /* OnTextChanged */
    @Test
    fun `event OnTextChanged → start loading, search products return success → stop loading, set data`() =
        runTest {
            val event = SearchUiEvent.OnTextChanged(word)
            effectHandler.process(event, defaultState, store)
            val expectedEvent = SearchEvent.DataLoaded(
                products = products
            )
            coVerify(exactly = 1) { store.dispatch(SearchEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 1) { store.dispatch(SearchEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event OnTextChanged → start loading, search products return success with empty body → show error toast`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { searchProducts(any()) } returns Result.Success()
            }
            val event = SearchUiEvent.OnTextChanged(word)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(SearchEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 1) { store.acceptNews(SearchNews.ShowErrorToast) }
        }

    @Test
    fun `event OnTextChanged → start loading, search products return empty error→ set empty error`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { searchProducts(any()) } returns Result.Error(SearchEmpty())
            }
            val event = SearchUiEvent.OnTextChanged(word)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(SearchEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 1) { store.dispatch(SearchEvent.IsEmpty) }
        }

    @Test
    fun `event OnTextChanged → start loading, search products return error → show toast`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { searchProducts(any()) } returns Result.Error()
            }
            val event = SearchUiEvent.OnTextChanged(word)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(SearchEvent.StartLoading) }
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 1) { store.acceptNews(SearchNews.ShowErrorToast) }
        }

    /* LoadMoreProducts */
    @Test
    fun `event LoadMoreProducts → start loading, search products return success → stop loading, set new data`() =
        runTest {
            val event = SearchUiEvent.LoadMoreProducts(word)
            effectHandler.process(event, defaultState, store)
            val expectedEvent = SearchEvent.DataLoaded(products = products)
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 1) { store.dispatch(SearchEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event LoadMoreProducts → start loading, search products return success with empty body → do nothing`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { searchProducts(any()) } returns Result.Success()
            }
            val event = SearchUiEvent.LoadMoreProducts(word)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 0) { store.acceptNews(SearchNews.ShowErrorToast) }
        }

    @Test
    fun `event LoadMoreProducts → start loading, search products return empty error → do nothing`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { searchProducts(any()) } returns Result.Error(SearchEmpty())
            }
            val event = SearchUiEvent.LoadMoreProducts(word)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 0) {  store.acceptNews(SearchNews.ShowErrorToast) }
        }

    @Test
    fun `event LoadMoreProducts → start loading, search products return error → do nothing`() =
        runTest {
            sharedSearchRepository.apply {
                coEvery { searchProducts(any()) } returns Result.Error()
            }
            val event = SearchUiEvent.LoadMoreProducts(word)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { sharedSearchRepository.searchProducts(word) }
            coVerify(exactly = 1) {  store.acceptNews(SearchNews.ShowErrorToast) }
        }

    /* OnBackClicked */
    @Test
    fun `event LoadMoreProducts → navigate back and clear data`() = runTest {
        val event = SearchUiEvent.OnBackClicked
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(SearchNews.NavigateBack) }
        coVerify(exactly = 1) { store.dispatch(SearchEvent.ClearData) }
    }

    /* OnProductClicked */
    @Test
    fun `event OnProductClicked → open product page`() = runTest {
        val event = SearchUiEvent.OnProductClicked(TestData.FIRST_PRODUCT_ID)
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(SearchNews.OpenProductPage(TestData.FIRST_PRODUCT_ID)) }
    }

    /* OnPlusClicked */
    @Test
    fun `event OnPlusClicked → set product count return success → update count`() = runTest {
        val event = SearchUiEvent.OnPlusClicked(
            TestData.FIRST_PRODUCT_ID,
            TestData.FIRST_PRODUCT_COUNT
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) {
            sharedSearchRepository.setProductCount(
                TestData.FIRST_PRODUCT_ID,
                TestData.FIRST_PRODUCT_COUNT
            )
        }
        coVerify(exactly = 1) {
            store.dispatch(
                SearchEvent.UpdateCount(
                    TestData.FIRST_PRODUCT_ID,
                    TestData.FIRST_PRODUCT_COUNT
                )
            )
        }
    }

    @Test
    fun `event OnPlusClicked → set product count return error → show error toast`() = runTest {
        sharedSearchRepository.apply {
            coEvery { setProductCount(any(), any()) } returns Result.Error()
        }
        val event = SearchUiEvent.OnPlusClicked(
            TestData.FIRST_PRODUCT_ID,
            TestData.FIRST_PRODUCT_COUNT
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) {
            sharedSearchRepository.setProductCount(
                TestData.FIRST_PRODUCT_ID,
                TestData.FIRST_PRODUCT_COUNT
            )
        }
        coVerify(exactly = 1) { store.acceptNews(SearchNews.ShowErrorToast) }
    }

    /* OnFilterClicked */
    @Test
    fun `event OnFilterClicked → open filter`() = runTest {
        val event = SearchUiEvent.OnFilterClicked
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(SearchNews.OpenFilter) }
    }
}