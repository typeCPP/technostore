package com.technostore.feature_review.review_list.presentation

import com.technostore.arch.mvi.Reducer

class ReviewListReducer : Reducer<ReviewListState, ReviewListEvent> {
    override fun reduce(currentState: ReviewListState, event: ReviewListEvent): ReviewListState {
        return when (event) {
            ReviewListEvent.StartLoading -> currentState.copy(isLoading = true)
            ReviewListEvent.EndLoading -> currentState.copy(isLoading = false)
            is ReviewListEvent.OnDataLoaded -> {
                currentState.copy(
                    allReviews = event.reviews,
                    positiveReviews = event.reviews.filter { it.rate in 7..10 },
                    neutralReviews = event.reviews.filter { it.rate in 4..6 },
                    negativeReviews = event.reviews.filter { it.rate in 1..3 },
                )
            }

            else -> currentState
        }
    }
}