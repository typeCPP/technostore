package com.technostore.review.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store

class ReviewEffectHandler : EffectHandler<ReviewState, ReviewEvent> {
    override suspend fun process(
        event: ReviewEvent,
        currentState: ReviewState,
        store: Store<ReviewState, ReviewEvent>
    ) {
        when (event) {
            ReviewUiEvent.OnBackClicked -> {
                store.acceptNews(ReviewNews.OpenPreviousPage)
            }

            else -> {}
        }
    }
}