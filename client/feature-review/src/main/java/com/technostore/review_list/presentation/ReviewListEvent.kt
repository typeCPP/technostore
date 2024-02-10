package com.technostore.review_list.presentation

import com.technostore.arch.mvi.Event
import com.technostore.business.model.ReviewModel

sealed class ReviewListEvent : Event {
    data object StartLoading : ReviewListEvent()
    data object EndLoading : ReviewListEvent()
    data class OnDataLoaded(val reviews: List<ReviewModel>) : ReviewListEvent()
}

sealed class ReviewListUiEvent : ReviewListEvent() {
    data class Init(val productId: Long) : ReviewListUiEvent()
    data object OnBackClicked : ReviewListUiEvent()
    data class OnReviewClicked(val review: ReviewModel) : ReviewListUiEvent()
    data object OnAllReviewsClicked : ReviewListUiEvent()
    data object OnNegativeReviewsClicked : ReviewListUiEvent()
    data object OnNeutralReviewsClicked : ReviewListUiEvent()
    data object OnPositiveReviewsClicked : ReviewListUiEvent()
}