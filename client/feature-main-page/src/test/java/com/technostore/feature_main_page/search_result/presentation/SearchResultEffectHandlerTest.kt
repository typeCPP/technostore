package com.technostore.feature_main_page.search_result.presentation

import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_main_page.business.MainRepository
import com.technostore.shared_search.business.SharedSearchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchResultEffectHandlerTest : SearchResultBaseTest() {
    private val sharedSearchRepositoryMock = mockk<SharedSearchRepository> {
        coEvery { setProductCount(any(), any()) } returns Result.Success()
    }
    private val mainRepositoryMock = mockk<MainRepository> {
        coEvery { searchByPopularity() } returns Result.Success(defaultProducts)
        coEvery { searchByCategory(any()) } returns Result.Success(defaultProducts)
    }
    private val effectHandler = SearchResultEffectHandler(
        sharedSearchRepository = sharedSearchRepositoryMock,
        mainRepository = mainRepositoryMock
    )
    private val defaultState = SearchResultState()

    /* Init */
    @Test
    fun `event init → search by popularity, categoryId = null → start loading, search by popularity return success → stop loading, set data`() =
        runTest {
            val event = SearchResultUiEvent.Init(isPopularity = true, categoryId = null)
            effectHandler.process(event, defaultState, store)
            val expectedEvent = SearchResultEvent.DataLoaded(defaultProducts)

            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByPopularity() }
            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
            coVerify(exactly = 0) { mainRepositoryMock.searchByCategory(any()) }
        }

    @Test
    fun `event init → search by popularity, categoryId = null → start loading, search by popularity return success with empty body → show error toast`() =
        runTest {
            mainRepositoryMock.apply {
                coEvery { searchByPopularity() } returns Result.Success()
            }
            val event = SearchResultUiEvent.Init(isPopularity = true, categoryId = null)
            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByPopularity() }
            coVerify(exactly = 1) { store.acceptNews(SearchResultNews.ShowErrorToast) }
            coVerify(exactly = 0) { mainRepositoryMock.searchByCategory(any()) }
        }

    @Test
    fun `event init → search by popularity, categoryId = null → start loading, search by popularity return error → show error toast`() =
        runTest {
            mainRepositoryMock.apply {
                coEvery { searchByPopularity() } returns Result.Error()
            }
            val event = SearchResultUiEvent.Init(isPopularity = true, categoryId = null)
            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByPopularity() }
            coVerify(exactly = 1) { store.acceptNews(SearchResultNews.ShowErrorToast) }
            coVerify(exactly = 0) { mainRepositoryMock.searchByCategory(any()) }
        }

    @Test
    fun `event init → search by popularity = false, categoryId not null → start loading, search by category return success → stop loading, set data`() =
        runTest {
            val event = SearchResultUiEvent.Init(
                isPopularity = false,
                categoryId = TestData.FIRST_CATEGORY_ID
            )
            effectHandler.process(event, defaultState, store)
            val expectedEvent = SearchResultEvent.DataLoaded(defaultProducts)

            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByCategory(TestData.FIRST_CATEGORY_ID) }
            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event init → search by popularity = false, categoryId not null → start loading, search by category return success with empty body → show error toast`() =
        runTest {
            mainRepositoryMock.apply {
                coEvery { searchByCategory(any()) } returns Result.Success()
            }
            val event = SearchResultUiEvent.Init(
                isPopularity = false,
                categoryId = TestData.FIRST_CATEGORY_ID
            )
            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByCategory(TestData.FIRST_CATEGORY_ID) }
            coVerify(exactly = 1) { store.acceptNews(SearchResultNews.ShowErrorToast) }
        }

    @Test
    fun `event init → search by popularity = false, categoryId not null → start loading, search by category return error → show error toast`() =
        runTest {
            mainRepositoryMock.apply {
                coEvery { searchByCategory(any()) } returns Result.Error()
            }
            val event = SearchResultUiEvent.Init(
                isPopularity = false,
                categoryId = TestData.FIRST_CATEGORY_ID
            )
            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.StartLoading) }
            coVerify(exactly = 1) { mainRepositoryMock.searchByCategory(TestData.FIRST_CATEGORY_ID) }
            coVerify(exactly = 1) { store.acceptNews(SearchResultNews.ShowErrorToast) }
        }

    @Test
    fun `event init → search by popularity = false, categoryId is null → show error toast`() =
        runTest {
            val event = SearchResultUiEvent.Init(isPopularity = false, categoryId = null)
            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(SearchResultEvent.StartLoading) }
            coVerify(exactly = 0) { mainRepositoryMock.searchByCategory(TestData.FIRST_CATEGORY_ID) }
            coVerify(exactly = 1) { store.acceptNews(SearchResultNews.ShowErrorToast) }
        }

    /* OnBackClicked */
    @Test
    fun `event OnBackClicked → navigate back`() = runTest {
        val event = SearchResultUiEvent.OnBackClicked
        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { store.acceptNews(SearchResultNews.NavigateBack) }
    }

    /* OnProductClicked */
    @Test
    fun `event OnProductClicked → open product page`() = runTest {
        val event = SearchResultUiEvent.OnProductClicked(TestData.FIRST_PRODUCT_ID)
        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { store.acceptNews(SearchResultNews.OpenProductPage(TestData.FIRST_PRODUCT_ID)) }
    }

    /* OnPlusClicked */
    @Test
    fun `event OnPlusClicked → set product count return success → update product count`() =
        runTest {
            val event = SearchResultUiEvent.OnPlusClicked(
                productId = TestData.FIRST_PRODUCT_ID,
                count = TestData.FIRST_PRODUCT_COUNT
            )
            val expectedEvent = SearchResultEvent.UpdateCount(
                productId = TestData.FIRST_PRODUCT_ID,
                count = TestData.FIRST_PRODUCT_COUNT
            )
            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event OnPlusClicked → set product count return error → show error toast`() = runTest {
        sharedSearchRepositoryMock.apply {
            coEvery { setProductCount(any(), any()) } returns Result.Error()
        }
        val event = SearchResultUiEvent.OnPlusClicked(
            productId = TestData.FIRST_PRODUCT_ID,
            count = TestData.FIRST_PRODUCT_COUNT
        )
        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { store.acceptNews(SearchResultNews.ShowErrorToast) }
    }
}