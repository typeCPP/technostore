package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.Store
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ProductDescriptionEffectHandlerTest {

    private val effectHandler = ProductDescriptionEffectHandler()
    private val defaultState = ProductDescriptionState()
    private val store = mockk<Store<ProductDescriptionState, ProductDescriptionUiEvent>>(relaxed = true)

    @Test
    fun `event OnBackClicked`() = runTest {
        val event = ProductDescriptionUiEvent.OnBackClicked
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) {  store.acceptNews(ProductDescriptionNews.OpenPreviousPage) }
    }
}