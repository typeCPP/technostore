package com.technostore.feature_shopping_cart.shopping_cart.ui

import com.technostore.common_test.TestData
import com.technostore.feature_shopping_cart.business.model.ProductOrderModel
import com.technostore.network.utils.URL
import org.junit.Assert.assertTrue
import org.junit.Test

class ShoppingCartDiffCallbackTest {
    private val firstModel = ProductOrderModel(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
        name = TestData.FIRST_PRODUCT_NAME,
        rating = TestData.FIRST_PRODUCT_RATING,
        count = TestData.FIRST_PRODUCT_COUNT,
        price = TestData.FIRST_PRODUCT_PRICE
    )
    private val secondModel = ProductOrderModel(
        id = TestData.SECOND_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.SECOND_PRODUCT_PHOTO_LINK}",
        price = TestData.SECOND_PRODUCT_PRICE,
        name = TestData.SECOND_PRODUCT_NAME,
        rating = TestData.SECOND_PRODUCT_RATING,
        count = TestData.SECOND_PRODUCT_COUNT,
    )
    private val newNegativeReviewModel = firstModel.copy(name = TestData.SECOND_PRODUCT_NAME)
    private val diffCallback = ShoppingCartDiffCallback()

    /* areItemsTheSame */
    @Test
    fun `is one item → areItemsTheSame return true`() {
        val result = diffCallback.areItemsTheSame(firstModel, firstModel)
        assertTrue(result)
    }

    @Test
    fun `is different items → areItemsTheSame return false`() {
        val result = diffCallback.areItemsTheSame(firstModel, secondModel)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals id → areItemsTheSame return true`() {
        val result = diffCallback.areItemsTheSame(firstModel, newNegativeReviewModel)
        assertTrue(result)
    }

    /* areContentsTheSame */
    @Test
    fun `is one item → areContentsTheSame return true`() {
        val result = diffCallback.areContentsTheSame(firstModel, firstModel)
        assertTrue(result)
    }

    @Test
    fun `is different items → areContentsTheSame return false`() {
        val result = diffCallback.areContentsTheSame(firstModel, secondModel)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals id → areContentsTheSame return false`() {
        val result = diffCallback.areContentsTheSame(firstModel, secondModel)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals content → areContentsTheSame return true`() {
        val newModel = ProductOrderModel(
            id = TestData.FIRST_PRODUCT_ID,
            photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
            name = TestData.FIRST_PRODUCT_NAME,
            rating = TestData.FIRST_PRODUCT_RATING,
            count = TestData.FIRST_PRODUCT_COUNT,
            price = TestData.FIRST_PRODUCT_PRICE
        )
        val result = diffCallback.areContentsTheSame(firstModel, newModel)
        assertTrue(result)
    }
}