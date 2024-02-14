package com.technostore.feature_review.review_list.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_review.business.model.ReviewModel

data class ReviewListState(
    val isLoading: Boolean = false,
    val productId: Long = 0,
    val allReviews: List<ReviewModel> = emptyList(),
    val positiveReviews: List<ReviewModel> = emptyList(),
    val negativeReviews: List<ReviewModel> = emptyList(),
    val neutralReviews: List<ReviewModel> = emptyList(),
) : State