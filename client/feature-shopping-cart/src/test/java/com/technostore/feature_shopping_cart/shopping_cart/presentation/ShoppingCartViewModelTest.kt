package com.technostore.feature_shopping_cart.shopping_cart.presentation

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
class ShoppingCartViewModelTest : ShoppingCartBaseTest() {
    private val viewModel = ShoppingCartViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        coVerify { store.dispatch(ShoppingCartUiEvent.Init) }
    }

    @Test
    fun `minus clicked → send OnMinusClicked event`() = runTest {
        viewModel.minusClicked(firstProduct)
        val expectedEvent = ShoppingCartUiEvent.OnMinusClicked(
            productId = firstProduct.id,
            count = firstProduct.count - 1
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `plus clicked → send OnPlusClicked event`() = runTest {
        viewModel.plusClicked(firstProduct)
        val expectedEvent = ShoppingCartUiEvent.OnPlusClicked(
            productId = firstProduct.id,
            count = firstProduct.count + 1
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `remove clicked → send OnRemoveClicked event`() = runTest {
        viewModel.removeClicked(TestData.FIRST_PRODUCT_ID)
        val expectedEvent = ShoppingCartUiEvent.OnRemoveClicked(
            productId = firstProduct.id,
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `set order clicked → send OnSetOrdersClicked event`() = runTest {
        viewModel.setOrderClicked()
        val expectedEvent = ShoppingCartUiEvent.OnSetOrdersClicked
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `start shopping clicked → send OnStartShoppingClicked event`() = runTest {
        viewModel.startShopping()
        val expectedEvent = ShoppingCartUiEvent.OnStartShoppingClicked
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }
}