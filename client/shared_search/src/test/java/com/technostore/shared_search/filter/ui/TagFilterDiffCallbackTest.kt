package com.technostore.shared_search.filter.ui

import com.technostore.common_test.TestData
import com.technostore.shared_search.business.model.Category
import com.technostore.shared_search.business.model.CategoryWithCheck
import org.junit.Assert.assertTrue
import org.junit.Test

class TagFilterDiffCallbackTest {
    private val firstCategory = Category(
        id = TestData.FIRST_CATEGORY_ID,
        name = TestData.FIRST_CATEGORY_NAME
    )
    private val secondCategory = Category(
        id = TestData.SECOND_CATEGORY_ID,
        name = TestData.SECOND_CATEGORY_NAME
    )
    private val firstModel = CategoryWithCheck(
        category = firstCategory,
        isSelected = false
    )
    private val secondModel =  CategoryWithCheck(
        category = secondCategory,
        isSelected = false
    )
    private val newNegativeReviewModel = firstModel.copy(isSelected = true)
    private val diffCallback = TagFilterDiffCallback()

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
        val newModel = CategoryWithCheck(
            category = firstCategory,
            isSelected = false
        )
        val result = diffCallback.areContentsTheSame(firstModel, newModel)
        assertTrue(result)
    }
}