package com.technostore.feature_product.product.business

import com.technostore.common_test.TestData
import com.technostore.feature_product.product.presentation.ProductUiEvent
import com.technostore.feature_product.product.presentation.ProductViewModel
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
class ProductViewModelTest : ProductBaseTest() {
    private val viewModel = ProductViewModel(store)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(ProductUiEvent.Init(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `buy clicked → send OnBuyClicked event`() = runTest {
        viewModel.onBuyClicked(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(ProductUiEvent.OnBuyClicked(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `rate clicked → send OnRateClicked event`() = runTest {
        viewModel.onRateClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ProductUiEvent.OnRateClicked) }
    }

    @Test
    fun `more description clicked → send OnMoreDescriptionClicked event`() = runTest {
        viewModel.onMoreDescriptionClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ProductUiEvent.OnMoreDescriptionClicked) }
    }

    @Test
    fun `more reviews clicked → send OnMoreReviewClicked event`() = runTest {
        viewModel.onMoreReviewsClicked(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(ProductUiEvent.OnMoreReviewClicked(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `back clicked clicked → send OnBackClicked event`() = runTest {
        viewModel.onClickBack()
        advanceUntilIdle()
        coVerify { store.dispatch(ProductUiEvent.OnBackClicked) }
    }

    @Test
    fun `set review clicked → send SetReview event`() = runTest {
        viewModel.setReview(
            rating = TestData.NEUTRAL_REVIEW_RATE,
            text = TestData.NEUTRAL_REVIEW_TEXT
        )
        val expectedEvent = ProductUiEvent.SetReview(
            rating = TestData.NEUTRAL_REVIEW_RATE,
            text = TestData.NEUTRAL_REVIEW_TEXT
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }
}