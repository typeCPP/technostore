package com.technostore.feature_review.review_list.ui

import com.technostore.common_test.TestData
import com.technostore.feature_review.business.model.ReviewModel
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtendedReviewDiffCallbackTest {
    private val negativeReviewModel = ReviewModel(
        id = TestData.NEGATIVE_REVIEW_ID,
        productId = TestData.NEGATIVE_REVIEW_PRODUCT_ID,
        text = TestData.NEGATIVE_REVIEW_TEXT,
        rate = TestData.NEGATIVE_REVIEW_RATE,
        date = TestData.NEGATIVE_REVIEW_DATE,
        userName = TestData.NEGATIVE_REVIEW_USER_NAME,
        photoLink = TestData.NEGATIVE_REVIEW_PHOTO_LINK,
    )
    private val neutralReviewModel = ReviewModel(
        id = TestData.NEUTRAL_REVIEW_ID,
        productId = TestData.NEUTRAL_REVIEW_PRODUCT_ID,
        text = TestData.NEUTRAL_REVIEW_TEXT,
        rate = TestData.NEUTRAL_REVIEW_RATE,
        date = TestData.NEUTRAL_REVIEW_DATE,
        userName = TestData.NEUTRAL_REVIEW_USER_NAME,
        photoLink = TestData.NEUTRAL_REVIEW_PHOTO_LINK,
    )
    private val newNegativeReviewModel =
        negativeReviewModel.copy(text = TestData.NEUTRAL_REVIEW_TEXT)
    private val diffCallback = ExtendedReviewDiffCallback()

    /* areItemsTheSame */
    @Test
    fun `is one item → areItemsTheSame return true`() {
        val result = diffCallback.areItemsTheSame(negativeReviewModel, negativeReviewModel)
        assertTrue(result)
    }

    @Test
    fun `is different items → areItemsTheSame return false`() {
        val result = diffCallback.areItemsTheSame(negativeReviewModel, neutralReviewModel)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals id → areItemsTheSame return true`() {
        val result = diffCallback.areItemsTheSame(negativeReviewModel, newNegativeReviewModel)
        assertTrue(result)
    }

    /* areContentsTheSame */
    @Test
    fun `is one item → areContentsTheSame return true`() {
        val result = diffCallback.areContentsTheSame(negativeReviewModel, negativeReviewModel)
        assertTrue(result)
    }

    @Test
    fun `is different items → areContentsTheSame return false`() {
        val result = diffCallback.areContentsTheSame(negativeReviewModel, neutralReviewModel)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals id → areContentsTheSame return false`() {
        val result = diffCallback.areContentsTheSame(negativeReviewModel, neutralReviewModel)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals content → areContentsTheSame return true`() {
        val newModel = ReviewModel(
            id = TestData.NEGATIVE_REVIEW_ID,
            productId = TestData.NEGATIVE_REVIEW_PRODUCT_ID,
            text = TestData.NEGATIVE_REVIEW_TEXT,
            rate = TestData.NEGATIVE_REVIEW_RATE,
            date = TestData.NEGATIVE_REVIEW_DATE,
            userName = TestData.NEGATIVE_REVIEW_USER_NAME,
            photoLink = TestData.NEGATIVE_REVIEW_PHOTO_LINK,
        )
        val result = diffCallback.areContentsTheSame(negativeReviewModel, newModel)
        assertTrue(result)
    }
}