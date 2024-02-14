package com.technostore.feature_review.review.presentation

import com.technostore.arch.mvi.News

sealed class ReviewNews : News() {
    data object OpenPreviousPage : ReviewNews()
}