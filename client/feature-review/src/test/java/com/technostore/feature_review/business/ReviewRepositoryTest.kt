package com.technostore.feature_review.business

import com.technostore.arch.result.Result
import com.technostore.feature_review.business.model.ReviewModel
import com.technostore.feature_review.business.model.mapper.ReviewMapper
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.ReviewServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.network.utils.URL
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat

class ReviewRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()

    private val reviewMapper = ReviewMapper()
    private val reviewRepository = ReviewRepositoryImpl(
        reviewService = networkModule.reviewService,
        reviewMapper = reviewMapper
    )
    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val firstExpectedReviewModel = ReviewModel(
        id = TestData.POSITIVE_REVIEW_ID,
        productId = TestData.POSITIVE_REVIEW_PRODUCT_ID,
        text = TestData.POSITIVE_REVIEW_TEXT,
        rate = TestData.POSITIVE_REVIEW_RATE,
        date = formatter.format(TestData.POSITIVE_REVIEW_DATE_LONG),
        photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${TestData.POSITIVE_REVIEW_PHOTO_LINK}",
        userName = TestData.POSITIVE_REVIEW_USER_NAME
    )
    private val secondExpectedReviewModel = ReviewModel(
        id = TestData.NEUTRAL_REVIEW_ID,
        productId = TestData.NEUTRAL_REVIEW_PRODUCT_ID,
        text = TestData.NEUTRAL_REVIEW_TEXT,
        rate = TestData.NEUTRAL_REVIEW_RATE,
        date = formatter.format(TestData.NEUTRAL_REVIEW_DATE_LONG),
        photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${TestData.NEUTRAL_REVIEW_PHOTO_LINK}",
        userName = TestData.NEUTRAL_REVIEW_USER_NAME
    )
    private val reviews = listOf(firstExpectedReviewModel, secondExpectedReviewModel)

    /* getReviews */
    @Test
    fun `get reviews with 200 status → return success with data`() = runTest {
        ReviewServiceMock {
            reviewsByProductId.success()
        }
        val result = reviewRepository.getReviews(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Success && result.data == reviews)
    }

    @Test
    fun `get reviews with 500 status → return error`() = runTest {
        ReviewServiceMock {
            reviewsByProductId.internalError()
        }
        val result = reviewRepository.getReviews(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Error)
    }
}