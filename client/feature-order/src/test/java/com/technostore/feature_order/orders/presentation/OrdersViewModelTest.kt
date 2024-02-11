package com.technostore.feature_order.orders.presentation

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
class OrdersViewModelTest : OrdersBaseTest() {
    private val viewModel = OrdersViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        coVerify { store.dispatch(OrdersUiEvent.Init) }
    }

    @Test
    fun `back clicked → send OnBackClicked event`() = runTest {
        viewModel.onBackClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(OrdersUiEvent.OnBackClicked) }
    }

    @Test
    fun `order clicked → send OnOrderClicked event`() = runTest {
        viewModel.orderClick(TestData.ORDER_ID)
        advanceUntilIdle()
        coVerify { store.dispatch(OrdersUiEvent.OnOrderClicked(TestData.ORDER_ID)) }
    }

    @Test
    fun `go to the shopping clicked → send OnStartShoppingClicked event`() = runTest {
        viewModel.onGoToShoppingClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(OrdersUiEvent.OnStartShoppingClicked) }
    }
}