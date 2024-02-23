package com.technostore.feature_review.review_list.presentation

import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(JUnitParamsRunner::class)
class ReviewListReducerTest : ReviewListBaseTest() {
    private val reducer = ReviewListReducer()
    private val defaultLoadingState = defaultState.copy(isLoading = true)

    /* StartLoading */
    @Test
    fun `event StartLoading → set is loading`() {
        val event = ReviewListEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → clear is loading`() {
        val event = ReviewListEvent.EndLoading
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading)
    }

    /* OnDataLoaded */
    @Test
    @Parameters("1,4,7", "3,6,10")
    fun `event OnDataLoaded → reviews with the left boundaries of condition → set data`(
        negativeRating: Int,
        neutralRating: Int,
        positiveRating: Int
    ) {
        val positiveReviewModel = positiveReviewModel.copy(rate = positiveRating)
        val neutralReviewModel = neutralReviewModel.copy(rate = neutralRating)
        val negativeReviewModel = negativeReviewModel.copy(rate = negativeRating)
        val allReviews = listOf(positiveReviewModel, neutralReviewModel, negativeReviewModel)
        val currentState = defaultState.copy(
            allReviews = allReviews,
            positiveReviews = emptyList(),
            neutralReviews = emptyList(),
            negativeReviews = emptyList()
        )
        val event = ReviewListEvent.OnDataLoaded(allReviews)
        val newState = reducer.reduce(currentState, event)
        assertTrue(
            newState.allReviews == allReviews &&
                    newState.positiveReviews == listOf(positiveReviewModel) &&
                    newState.neutralReviews == listOf(neutralReviewModel) &&
                    newState.negativeReviews == listOf(negativeReviewModel)
        )
    }

    @Test
    fun `event OnDataLoaded → set data`() {
        val positiveReviewModel = positiveReviewModel.copy(rate = 8)
        val neutralReviewModel = neutralReviewModel.copy(rate = 5)
        val negativeReviewModel = negativeReviewModel.copy(rate = 2)
        val allReviews = listOf(positiveReviewModel, neutralReviewModel, negativeReviewModel)
        val currentState = defaultState.copy(
            allReviews = allReviews,
            positiveReviews = emptyList(),
            neutralReviews = emptyList(),
            negativeReviews = emptyList()
        )
        val event = ReviewListEvent.OnDataLoaded(allReviews)
        val newState = reducer.reduce(currentState, event)
        assertTrue(
            newState.allReviews == allReviews &&
                    newState.positiveReviews == listOf(positiveReviewModel) &&
                    newState.neutralReviews == listOf(neutralReviewModel) &&
                    newState.negativeReviews == listOf(negativeReviewModel)
        )
    }


    /* UI event */
    @Test
    fun `ui event → return current state`() {
        val event = ReviewListUiEvent.OnBackClicked
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}