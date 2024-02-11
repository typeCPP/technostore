package com.technostore.review.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.TestData
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {
    private val store = mockk<Store<ReviewState, ReviewEvent>>(relaxed = true)
    private val viewModel = ReviewViewModel(store)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init(
            userName = TestData.NEGATIVE_REVIEW_USER_NAME,
            photoLink = TestData.NEGATIVE_REVIEW_PHOTO_LINK,
            date = TestData.NEGATIVE_REVIEW_DATE,
            rating = TestData.NEGATIVE_REVIEW_RATE,
            text = TestData.NEGATIVE_REVIEW_TEXT
        )
        val expectedEvent = ReviewUiEvent.Init(
            userName = TestData.NEGATIVE_REVIEW_USER_NAME,
            photoLink = TestData.NEGATIVE_REVIEW_PHOTO_LINK,
            date = TestData.NEGATIVE_REVIEW_DATE,
            rating = TestData.NEGATIVE_REVIEW_RATE,
            text = TestData.NEGATIVE_REVIEW_TEXT
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `back clicked → send OnBackClicked event`() = runTest {
        viewModel.onClickBack()
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewUiEvent.OnBackClicked) }
    }
}