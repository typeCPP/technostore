package com.technostore.feature_product.product.business

import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_product.business.ProductRepository
import com.technostore.feature_product.product.presentation.ProductEffectHandler
import com.technostore.feature_product.product.presentation.ProductEvent
import com.technostore.feature_product.product.presentation.ProductNews
import com.technostore.feature_product.product.presentation.ProductState
import com.technostore.feature_product.product.presentation.ProductUiEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductEffectHandlerTest : ProductBaseTest() {
    private val productRepository = mockk<ProductRepository> {
        coEvery { getProductById(any()) } returns Result.Success(productDetailModel)
        coEvery { getUserReview(any()) } returns Result.Success(userReview)
        coEvery { setProductCount(any(), any()) } returns Result.Success()
        coEvery { setReview(any(), any(), any()) } returns Result.Success()
        coEvery { getReviews(any()) } returns Result.Success(listOf(reviewModel))
    }
    private val productEffectHandler = ProductEffectHandler(
        productRepository = productRepository
    )
    private val defaultState = ProductState()

    /* Init */
    @Test
    fun `event init → start loading, get product return success, get reviews return success → set user review, stop loading, set data`() =
        runTest {
            val event = ProductUiEvent.Init(productId = TestData.FIRST_PRODUCT_ID)
            productEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ProductEvent.StartLoading) }
            coVerify(exactly = 1) { productRepository.getUserReview(event.productId) }
            coVerify(exactly = 1) { store.dispatch(ProductEvent.OnReviewLoaded(TestData.USER_REVIEW_TEXT)) }
            coVerify(exactly = 1) { store.dispatch(ProductEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(ProductEvent.OnDataLoaded(productDetailModel)) }
        }

    @Test
    fun `event init → start loading, get product return success, get reviews return error → stop loading, set data`() =
        runTest {
            productRepository.apply {
                coEvery { getUserReview(any()) } returns Result.Error()
            }
            val event = ProductUiEvent.Init(productId = TestData.FIRST_PRODUCT_ID)
            productEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ProductEvent.StartLoading) }
            coVerify(exactly = 1) { productRepository.getUserReview(event.productId) }
            coVerify(exactly = 0) { store.dispatch(ProductEvent.OnReviewLoaded(TestData.USER_REVIEW_TEXT)) }
            coVerify(exactly = 1) { store.dispatch(ProductEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(ProductEvent.OnDataLoaded(productDetailModel)) }
        }

    @Test
    fun `event init → start loading, get product return error → does not call get user review, show error`() =
        runTest {
            productRepository.apply {
                coEvery { getProductById(any()) } returns Result.Error()
            }
            val event = ProductUiEvent.Init(productId = TestData.FIRST_PRODUCT_ID)
            productEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ProductEvent.StartLoading) }
            coVerify(exactly = 0) { productRepository.getUserReview(event.productId) }
            coVerify(exactly = 1) { store.acceptNews(ProductNews.ShowErrorToast) }
        }

    @Test
    fun `event init → start loading, get product return success without body → does not call get user review, show error`() =
        runTest {
            productRepository.apply {
                coEvery { getProductById(any()) } returns Result.Success()
            }
            val event = ProductUiEvent.Init(productId = TestData.FIRST_PRODUCT_ID)
            productEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ProductEvent.StartLoading) }
            coVerify(exactly = 0) { productRepository.getUserReview(event.productId) }
            coVerify(exactly = 1) { store.acceptNews(ProductNews.ShowErrorToast) }
        }

    /* OnRateClicked */
    @Test
    fun `event OnRateClicked → productDetail is empty → open rate dialog with 0 userReview`() =
        runTest {
            val event = ProductUiEvent.OnRateClicked
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(
                    userReviewText = TestData.USER_REVIEW_TEXT,
                    productDetail = null
                ),
                store = store
            )
            val expectedNews = ProductNews.OpenRateDialog(
                reviewText = TestData.USER_REVIEW_TEXT,
                userRating = 0
            )
            coVerify(exactly = 1) {
                store.acceptNews(expectedNews)
            }
        }

    @Test
    fun `event OnRateClicked → text is empty → open rate dialog with empty text`() =
        runTest {
            val event = ProductUiEvent.OnRateClicked
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(
                    userReviewText = null,
                    productDetail = productDetailModel
                ),
                store = store
            )
            val expectedNews = ProductNews.OpenRateDialog(
                reviewText = null,
                userRating = productDetailModel.userRating
            )
            coVerify(exactly = 1) { store.acceptNews(expectedNews) }
        }

    /* OnBuyClicked */
    @Test
    fun `event OnBuyClicked → product detail is not empty, set product count return seccess → update product count`() =
        runTest {
            val newCount = TestData.FIRST_PRODUCT_COUNT + 1
            val event = ProductUiEvent.OnBuyClicked(productId = TestData.FIRST_PRODUCT_ID)
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            val expectedEvent = ProductEvent.UpdateInCartCount(newCount)
            coVerify(exactly = 1) { store.dispatch(expectedEvent) }
        }

    @Test
    fun `event OnBuyClicked → product detail is not empty, set product count return error → show error`() =
        runTest {
            productRepository.apply {
                coEvery { setProductCount(any(), any()) } returns Result.Error()
            }
            val newCount = TestData.FIRST_PRODUCT_COUNT + 1
            val event = ProductUiEvent.OnBuyClicked(productId = TestData.FIRST_PRODUCT_ID)
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            coVerify(exactly = 1) { store.acceptNews(ProductNews.ShowErrorToast) }
            coVerify(exactly = 0) { store.dispatch(ProductEvent.UpdateInCartCount(newCount)) }
        }

    @Test
    fun `event OnBuyClicked → product detail is empty → try to set product count = 1`() = runTest {
        val event = ProductUiEvent.OnBuyClicked(productId = TestData.FIRST_PRODUCT_ID)
        productEffectHandler.process(
            event = event,
            currentState = defaultState,
            store = store
        )
        coVerify(exactly = 1) { store.dispatch(ProductEvent.UpdateInCartCount(1)) }
    }

    /* OnMoreDescriptionClicked */
    @Test
    fun `event OnMoreDescriptionClicked → product detail is not empty → open description with not empty name and description`() =
        runTest {
            val event = ProductUiEvent.OnMoreDescriptionClicked
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            val expectedNews = ProductNews.OpenDescription(
                productName = TestData.FIRST_PRODUCT_NAME,
                description = TestData.FIRST_PRODUCT_DESCRIPTION
            )
            coVerify(exactly = 1) { store.acceptNews(expectedNews) }
        }

    @Test
    fun `event OnMoreDescriptionClicked → product detail is empty → open description empty name and description`() =
        runTest {
            val event = ProductUiEvent.OnMoreDescriptionClicked
            productEffectHandler.process(
                event = event,
                currentState = defaultState,
                store = store
            )
            val expectedNews = ProductNews.OpenDescription(
                productName = "",
                description = ""
            )
            coVerify(exactly = 1) { store.acceptNews(expectedNews) }
        }

    /* OnMoreReviewClicked */
    @Test
    fun `event OnMoreReviewClicked → open reviews page`() =
        runTest {
            val event = ProductUiEvent.OnMoreReviewClicked(TestData.FIRST_PRODUCT_ID)
            productEffectHandler.process(
                event = event,
                currentState = defaultState,
                store = store
            )
            val expectedNews = ProductNews.OpenReviewsListPage(TestData.FIRST_PRODUCT_ID)
            coVerify(exactly = 1) { store.acceptNews(expectedNews) }
        }

    /* OnBackClicked */
    @Test
    fun `event OnBackClicked → open previous page`() = runTest {
        val event = ProductUiEvent.OnBackClicked
        productEffectHandler.process(
            event = event,
            currentState = defaultState,
            store = store
        )
        coVerify(exactly = 1) { store.acceptNews(ProductNews.OpenPreviousPage) }
    }

    /* SetReview */
    @Test
    fun `event SetReview, text is empty → set review return success → get reviews return success with not empty body → update reviews and rating`() =
        runTest {
            val event = ProductUiEvent.SetReview(rating = TestData.USER_REVIEW_RATE, text = null)
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            coVerify(exactly = 1) {
                productRepository.setReview(
                    TestData.FIRST_PRODUCT_ID,
                    TestData.USER_REVIEW_RATE,
                    null
                )
            }
            coVerify(exactly = 1) { productRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) { store.dispatch(ProductEvent.UpdateReviews(listOf(reviewModel))) }
            coVerify(exactly = 1) {
                store.dispatch(
                    ProductEvent.UpdateRating(
                        TestData.USER_REVIEW_RATE,
                        null
                    )
                )
            }
        }

    @Test
    fun `event SetReview, text is not empty → set review return success → get reviews return success with not empty body → update reviews and rating`() =
        runTest {
            val event = ProductUiEvent.SetReview(
                rating = TestData.USER_REVIEW_RATE,
                text = TestData.USER_REVIEW_TEXT
            )
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            coVerify(exactly = 1) {
                productRepository.setReview(
                    productId = TestData.FIRST_PRODUCT_ID,
                    rating = TestData.USER_REVIEW_RATE,
                    text = TestData.USER_REVIEW_TEXT
                )
            }
            coVerify(exactly = 1) { productRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) { store.dispatch(ProductEvent.UpdateReviews(listOf(reviewModel))) }
            coVerify(exactly = 1) {
                store.dispatch(
                    ProductEvent.UpdateRating(
                        TestData.USER_REVIEW_RATE,
                        TestData.USER_REVIEW_TEXT
                    )
                )
            }
        }

    @Test
    fun `event SetReview, text is not empty → set review return success → get reviews return error → update rating`() =
        runTest {
            productRepository.apply {
                coEvery { getReviews(any()) } returns Result.Error()
            }
            val event = ProductUiEvent.SetReview(
                rating = TestData.USER_REVIEW_RATE,
                text = TestData.USER_REVIEW_TEXT
            )
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            coVerify(exactly = 1) {
                productRepository.setReview(
                    productId = TestData.FIRST_PRODUCT_ID,
                    rating = TestData.USER_REVIEW_RATE,
                    text = TestData.USER_REVIEW_TEXT
                )
            }
            coVerify(exactly = 1) { productRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) {
                store.dispatch(
                    ProductEvent.UpdateRating(
                        TestData.USER_REVIEW_RATE,
                        TestData.USER_REVIEW_TEXT
                    )
                )
            }
        }

    @Test
    fun `event SetReview, text is not empty → set review return success → get reviews return success with empty body → update rating`() =
        runTest {
            productRepository.apply {
                coEvery { getReviews(any()) } returns Result.Success()
            }
            val event = ProductUiEvent.SetReview(
                rating = TestData.USER_REVIEW_RATE,
                text = TestData.USER_REVIEW_TEXT
            )
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            coVerify(exactly = 1) {
                productRepository.setReview(
                    productId = TestData.FIRST_PRODUCT_ID,
                    rating = TestData.USER_REVIEW_RATE,
                    text = TestData.USER_REVIEW_TEXT
                )
            }
            coVerify(exactly = 1) { productRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) {
                store.dispatch(
                    ProductEvent.UpdateRating(
                        TestData.USER_REVIEW_RATE,
                        TestData.USER_REVIEW_TEXT
                    )
                )
            }
        }

    @Test
    fun `event SetReview, text is not empty → set review return error → get reviews does not call, show error toast`() =
        runTest {
            productRepository.apply {
                coEvery { setReview(any(), any(), any()) } returns Result.Error()
            }
            val event = ProductUiEvent.SetReview(
                rating = TestData.USER_REVIEW_RATE,
                text = TestData.USER_REVIEW_TEXT
            )
            productEffectHandler.process(
                event = event,
                currentState = defaultState.copy(productDetail = productDetailModel),
                store = store
            )
            coVerify(exactly = 1) {
                productRepository.setReview(
                    productId = TestData.FIRST_PRODUCT_ID,
                    rating = TestData.USER_REVIEW_RATE,
                    text = TestData.USER_REVIEW_TEXT
                )
            }
            coVerify(exactly = 0) { productRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) { store.acceptNews(ProductNews.ShowErrorToast) }
        }

    @Test
    fun `event SetReview, product detail is empty → set review does not call, show error toast`() =
        runTest {
            val event = ProductUiEvent.SetReview(
                rating = TestData.USER_REVIEW_RATE,
                text = TestData.USER_REVIEW_TEXT
            )
            productEffectHandler.process(
                event = event,
                currentState = defaultState,
                store = store
            )
            coVerify(exactly = 1) { store.acceptNews(ProductNews.ShowErrorToast) }
            coVerify(exactly = 0) {
                productRepository.setReview(
                    productId = TestData.FIRST_PRODUCT_ID,
                    rating = TestData.USER_REVIEW_RATE,
                    text = TestData.USER_REVIEW_TEXT
                )
            }
        }
}
