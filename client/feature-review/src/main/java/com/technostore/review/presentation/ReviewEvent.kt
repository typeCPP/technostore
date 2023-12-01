package com.technostore.review.presentation

import com.technostore.arch.mvi.Event

sealed class ReviewEvent : Event {
}

sealed class ReviewUiEvent : ReviewEvent() {
    data class Init(
        val userName: String,
        val photoLink: String,
        val date: String,
        val rating: Int,
        val text: String
    ) : ReviewUiEvent()

    data object OnBackClicked : ReviewUiEvent()
}