package com.technostore.shared_search.ui

import com.technostore.common_test.TestData
import com.technostore.network.utils.URL
import com.technostore.shared_search.business.model.ProductSearchModel
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductDiffCallbackTest {
    private val firstModel = ProductSearchModel(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
        price = TestData.FIRST_PRODUCT_PRICE,
        name = TestData.FIRST_PRODUCT_NAME,
        rating = TestData.FIRST_PRODUCT_RATING,
        inCartCount = TestData.FIRST_PRODUCT_COUNT,
        reviewCount = TestData.FIRST_PRODUCT_REVIEW_COUNT
    )
    private val secondModel = ProductSearchModel(
        id = TestData.SECOND_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.SECOND_PRODUCT_PHOTO_LINK}",
        price = TestData.SECOND_PRODUCT_PRICE,
        name = TestData.SECOND_PRODUCT_NAME,
        rating = TestData.SECOND_PRODUCT_RATING,
        inCartCount = TestData.SECOND_PRODUCT_COUNT,
        reviewCount = TestData.SECOND_PRODUCT_REVIEW_COUNT
    )
    private val newNegativeReviewModel = firstModel.copy(name = TestData.SECOND_PRODUCT_NAME)
    private val diffCallback = ProductDiffCallback()

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
        val newModel = ProductSearchModel(
            id = TestData.FIRST_PRODUCT_ID,
            photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
            price = TestData.FIRST_PRODUCT_PRICE,
            name = TestData.FIRST_PRODUCT_NAME,
            rating = TestData.FIRST_PRODUCT_RATING,
            inCartCount = TestData.FIRST_PRODUCT_COUNT,
            reviewCount = TestData.FIRST_PRODUCT_REVIEW_COUNT
        )
        val result = diffCallback.areContentsTheSame(firstModel, newModel)
        assertTrue(result)
    }
}