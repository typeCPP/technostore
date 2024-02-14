package com.technostore.feature_order.order_detail.presentation

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
class OrderDetailViewModelTest : OrderDetailBaseTest() {
    private val viewModel = OrderDetailViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init(TestData.FIRST_ORDER_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(OrderDetailUiEvent.Init(TestData.FIRST_ORDER_ID)) }
    }

    @Test
    fun `product clicked → send OnProductClicked event`() = runTest {
        viewModel.productClicked(TestData.FIRST_PRODUCT_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(OrderDetailUiEvent.OnProductClicked(TestData.FIRST_PRODUCT_ID)) }
    }
    @Test
    fun `back clicked → send OnBackClicked event`() = runTest {
        viewModel.onBackClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(OrderDetailUiEvent.OnBackClicked) }
    }
}