package com.technostore.review_list.presentation

import com.technostore.arch.mvi.News
import com.technostore.business.model.ReviewModel

sealed class ReviewListNews : News() {
    data object ShowErrorToast : ReviewListNews()
    data class OpenReviewPage(val review: ReviewModel) : ReviewListNews()
    data object OpenPreviousPage : ReviewListNews()
    data class ShowReviews(val reviews: List<ReviewModel>) : ReviewListNews()
}