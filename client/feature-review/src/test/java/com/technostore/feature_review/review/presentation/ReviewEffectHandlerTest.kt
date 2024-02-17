package com.technostore.feature_review.review.presentation

import com.technostore.arch.mvi.Store
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ReviewEffectHandlerTest {

    private val effectHandler = ReviewEffectHandler()
    private val store = mockk<Store<ReviewState, ReviewEvent>>(relaxed = true)
    private val defaultState = ReviewState()

    @Test
    fun `event OnBackClicked`() = runTest {
        val event = ReviewUiEvent.OnBackClicked

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(ReviewNews.OpenPreviousPage) }
    }
}