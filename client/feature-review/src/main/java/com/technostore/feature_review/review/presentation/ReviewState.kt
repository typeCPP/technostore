package com.technostore.feature_review.review.presentation

import com.technostore.arch.mvi.State

data class ReviewState(
    val userName: String = "",
    val photoLink: String = "",
    val date: String = "",
    val rating: Int = 1,
    val text: String = ""
) : State