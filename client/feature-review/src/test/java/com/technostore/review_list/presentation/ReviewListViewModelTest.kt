package com.technostore.review_list.presentation

import com.technostore.common_test.TestData
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewListViewModelTest : ReviewListBaseTest() {
    private val viewModel = ReviewListViewModel(store)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewListUiEvent.Init(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `back clicked → send OnBackClicked event`() = runTest {
        viewModel.onClickBack()
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewListUiEvent.OnBackClicked) }
    }

    @Test
    fun `review clicked → send OnBackClicked event`() = runTest {
        viewModel.onReviewClicked(neutralReviewModel)
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewListUiEvent.OnReviewClicked(neutralReviewModel)) }
    }

    @Test
    fun `all reviews clicked → send OnAllReviewsClicked event`() = runTest {
        viewModel.onAllReviewsClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewListUiEvent.OnAllReviewsClicked) }
    }

    @Test
    fun `positive reviews clicked → send OnAllReviewsClicked event`() = runTest {
        viewModel.onPositiveReviewsClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewListUiEvent.OnPositiveReviewsClicked) }
    }

    @Test
    fun `negative reviews clicked → send OnNegativeReviewsClicked event`() = runTest {
        viewModel.onNegativeReviewsClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewListUiEvent.OnNegativeReviewsClicked) }
    }

    @Test
    fun `neutral reviews clicked → send OnNeutralReviewsClicked event`() = runTest {
        viewModel.onNeutralReviewsClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ReviewListUiEvent.OnNeutralReviewsClicked) }
    }
}