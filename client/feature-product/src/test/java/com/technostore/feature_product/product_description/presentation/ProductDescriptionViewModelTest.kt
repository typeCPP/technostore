package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.Store
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
class ProductDescriptionViewModelTest {
    private val store =
        mockk<Store<ProductDescriptionState, ProductDescriptionUiEvent>>(relaxed = true)
    private val viewModel = ProductDescriptionViewModel(store)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `back clicked → send OnBackClicked event`() = runTest {
        viewModel.onBackClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ProductDescriptionUiEvent.OnBackClicked) }
    }
}