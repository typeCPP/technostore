package com.technostore.review.presentation

import com.technostore.arch.mvi.Reducer

class ReviewReducer : Reducer<ReviewState, ReviewEvent> {
    override fun reduce(currentState: ReviewState, event: ReviewEvent): ReviewState {
        return when (event) {
            is ReviewUiEvent.Init -> {
                currentState.copy(
                    userName = event.userName,
                    photoLink = event.photoLink,
                    date = event.date,
                    rating = event.rating,
                    text = event.text
                )
            }

            else -> currentState
        }
    }
}