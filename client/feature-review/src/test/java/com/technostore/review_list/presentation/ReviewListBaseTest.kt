package com.technostore.review_list.presentation

import com.technostore.business.model.ReviewModel
import com.technostore.common_test.TestData

open class ReviewListBaseTest {
    protected val defaultState = ReviewListState()
    protected val negativeReviewModel = ReviewModel(
        id = TestData.NEGATIVE_REVIEW_ID,
        productId = TestData.NEGATIVE_REVIEW_PRODUCT_ID,
        text = TestData.NEGATIVE_REVIEW_TEXT,
        rate = TestData.NEGATIVE_REVIEW_RATE,
        date = TestData.NEGATIVE_REVIEW_DATE,
        userName = TestData.NEGATIVE_REVIEW_USER_NAME,
        photoLink = TestData.NEGATIVE_REVIEW_PHOTO_LINK,
    )
    protected val neutralReviewModel = ReviewModel(
        id = TestData.NEUTRAL_REVIEW_ID,
        productId = TestData.NEUTRAL_REVIEW_PRODUCT_ID,
        text = TestData.NEUTRAL_REVIEW_TEXT,
        rate = TestData.NEUTRAL_REVIEW_RATE,
        date = TestData.NEUTRAL_REVIEW_DATE,
        userName = TestData.NEUTRAL_REVIEW_USER_NAME,
        photoLink = TestData.NEUTRAL_REVIEW_PHOTO_LINK,
    )
    protected val positiveReviewModel = ReviewModel(
        id = TestData.POSITIVE_REVIEW_ID,
        productId = TestData.POSITIVE_REVIEW_PRODUCT_ID,
        text = TestData.POSITIVE_REVIEW_TEXT,
        rate = TestData.POSITIVE_REVIEW_RATE,
        date = TestData.POSITIVE_REVIEW_DATE,
        userName = TestData.POSITIVE_REVIEW_USER_NAME,
        photoLink = TestData.POSITIVE_REVIEW_PHOTO_LINK,
    )
    protected val reviews = listOf(negativeReviewModel, neutralReviewModel, positiveReviewModel)
}