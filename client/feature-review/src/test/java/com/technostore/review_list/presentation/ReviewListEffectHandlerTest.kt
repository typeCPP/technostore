package com.technostore.review_list.presentation

import com.technostore.arch.result.Result
import com.technostore.business.ReviewRepository
import com.technostore.common_test.TestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ReviewListEffectHandlerTest : ReviewListBaseTest() {
    private val reviewRepository = mockk<ReviewRepository> {
        coEvery { getReviews(any()) } returns Result.Success(reviews)
    }
    private val effectHandler = ReviewListEffectHandler(
        reviewRepository = reviewRepository
    )

    /* Init */
    @Test
    fun `event Init → start loading, get reviews return success → stop loading, set data`() =
        runTest {
            val event = ReviewListUiEvent.Init(productId = TestData.FIRST_PRODUCT_ID)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ReviewListEvent.StartLoading) }
            coVerify(exactly = 1) { reviewRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) { store.dispatch(ReviewListEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(ReviewListEvent.OnDataLoaded(reviews)) }
            coVerify(exactly = 1) { store.acceptNews(ReviewListNews.ShowReviews(reviews)) }
        }

    @Test
    fun `event Init → start loading, get reviews return success with empty body → show error toast`() =
        runTest {
            reviewRepository.apply {
                coEvery { getReviews(any()) } returns Result.Success()
            }
            val event = ReviewListUiEvent.Init(productId = TestData.FIRST_PRODUCT_ID)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ReviewListEvent.StartLoading) }
            coVerify(exactly = 1) { reviewRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) { store.acceptNews(ReviewListNews.ShowErrorToast) }
        }

    @Test
    fun `event Init → start loading, get reviews return error → show error toast`() =
        runTest {
            reviewRepository.apply {
                coEvery { getReviews(any()) } returns Result.Error()
            }
            val event = ReviewListUiEvent.Init(productId = TestData.FIRST_PRODUCT_ID)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ReviewListEvent.StartLoading) }
            coVerify(exactly = 1) { reviewRepository.getReviews(TestData.FIRST_PRODUCT_ID) }
            coVerify(exactly = 1) { store.acceptNews(ReviewListNews.ShowErrorToast) }
        }

    /* OnBackClicked */
    @Test
    fun `event OnBackClicked → open prev page`() = runTest {
        val event = ReviewListUiEvent.OnBackClicked
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(ReviewListNews.OpenPreviousPage) }
    }

    /* OnReviewClicked */
    @Test
    fun `event OnReviewClicked → open review page`() = runTest {
        val event = ReviewListUiEvent.OnReviewClicked(negativeReviewModel)
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(ReviewListNews.OpenReviewPage(negativeReviewModel)) }
    }

    /* OnAllReviewsClicked */
    @Test
    fun `event OnAllReviewsClicked → show all reviews`() = runTest {
        val currentState = defaultState.copy(
            allReviews = reviews
        )
        val event = ReviewListUiEvent.OnAllReviewsClicked
        effectHandler.process(event, currentState, store)
        coVerify(exactly = 1) { store.acceptNews(ReviewListNews.ShowReviews(reviews)) }
    }

    /* OnNegativeReviewsClicked */
    @Test
    fun `event OnNegativeReviewsClicked → show negative reviews`() = runTest {
        val negativeReviews = listOf(negativeReviewModel)
        val currentState = defaultState.copy(
            allReviews = reviews,
            negativeReviews = negativeReviews
        )
        val event = ReviewListUiEvent.OnNegativeReviewsClicked
        effectHandler.process(event, currentState, store)
        coVerify(exactly = 1) { store.acceptNews(ReviewListNews.ShowReviews(negativeReviews)) }
    }

    /* OnNeutralReviewsClicked */
    @Test
    fun `event OnNeutralReviewsClicked → show neutral reviews`() = runTest {
        val neutralReviews = listOf(neutralReviewModel)
        val currentState = defaultState.copy(
            allReviews = reviews,
            neutralReviews = neutralReviews
        )
        val event = ReviewListUiEvent.OnNeutralReviewsClicked
        effectHandler.process(event, currentState, store)
        coVerify(exactly = 1) { store.acceptNews(ReviewListNews.ShowReviews(neutralReviews)) }
    }

    /* OnPositiveReviewsClicked */
    @Test
    fun `event OnPositiveReviewsClicked → show positive reviews`() = runTest {
        val positiveReviews = listOf(positiveReviewModel)
        val currentState = defaultState.copy(
            allReviews = reviews,
            positiveReviews = positiveReviews
        )
        val event = ReviewListUiEvent.OnPositiveReviewsClicked
        effectHandler.process(event, currentState, store)
        coVerify(exactly = 1) { store.acceptNews(ReviewListNews.ShowReviews(positiveReviews)) }
    }
}