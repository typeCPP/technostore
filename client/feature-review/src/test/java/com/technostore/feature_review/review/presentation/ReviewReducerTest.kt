package com.technostore.feature_review.review.presentation

import com.technostore.common_test.TestData
import org.junit.Assert
import org.junit.Test

class ReviewReducerTest {

    private val reviewReducer = ReviewReducer()
    private val defaultState = ReviewState()

    @Test
    fun `event Init → set data`() {
        val event = ReviewUiEvent.Init(
            userName = TestData.NAME,
            photoLink = TestData.USER_PHOTO_LINK,
            date = TestData.NEUTRAL_REVIEW_DATE,
            rating = TestData.RATING,
            text = TestData.NEUTRAL_REVIEW_TEXT
        )
        val newState = reviewReducer.reduce(defaultState.copy(), event)
        Assert.assertTrue(newState.date == TestData.NEUTRAL_REVIEW_DATE)
        Assert.assertTrue(newState.userName == TestData.NAME)
        Assert.assertTrue(newState.photoLink == TestData.USER_PHOTO_LINK)
        Assert.assertTrue(newState.rating == TestData.RATING)
        Assert.assertTrue(newState.text == TestData.NEUTRAL_REVIEW_TEXT)
    }

    @Test
    fun `event OnBackClicked → return default state`() {
        val event = ReviewUiEvent.OnBackClicked
        val newState = reviewReducer.reduce(defaultState, event)
        Assert.assertTrue(newState == defaultState)
    }

}