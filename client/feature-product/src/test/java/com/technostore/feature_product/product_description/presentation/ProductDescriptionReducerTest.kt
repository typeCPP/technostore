package com.technostore.feature_product.product_description.presentation

import org.junit.Assert
import org.junit.Test

class ProductDescriptionReducerTest {

    private val productDescriptionReducer = ProductDescriptionReducer()
    private val productDescriptionState = ProductDescriptionState()

    @Test
    fun `init`() {
        val event = ProductDescriptionUiEvent.OnBackClicked
        val newState = productDescriptionReducer.reduce(productDescriptionState, event)
        Assert.assertTrue(newState == productDescriptionState)
    }
}