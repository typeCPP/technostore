package com.technostore.review.presentation

import com.technostore.arch.mvi.News

sealed class ReviewNews : News() {
    data object OpenPreviousPage : ReviewNews()
}