package com.technostore.feature_main_page.main_page.ui

import com.technostore.common_test.TestData
import com.technostore.shared_search.business.model.Category
import com.technostore.shared_search.business.model.CategoryWithCheck
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoryDiffCallbackTest {
    private val diffCallback = CategoryDiffCallback()
    private val firstCategory = Category(
        id = TestData.FIRST_CATEGORY_ID,
        name = TestData.FIRST_CATEGORY_NAME
    )
    private val secondCategory = Category(
        id = TestData.SECOND_CATEGORY_ID,
        name = TestData.SECOND_CATEGORY_NAME
    )
    private val firstCategoryWithCheck = CategoryWithCheck(
        category = firstCategory,
        isSelected = false
    )
    private val secondCategoryWithCheck = CategoryWithCheck(
        category = secondCategory,
        isSelected = false
    )

    /* areItemsTheSame */
    @Test
    fun `is one item → areItemsTheSame return true`() {
        val result = diffCallback.areItemsTheSame(firstCategoryWithCheck, firstCategoryWithCheck)
        assertTrue(result)
    }

    @Test
    fun `is different items → areItemsTheSame return false`() {
        val result = diffCallback.areItemsTheSame(firstCategoryWithCheck, secondCategoryWithCheck)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals id → areItemsTheSame return true`() {
        val newCategoryWithCheck = CategoryWithCheck(
            category = firstCategory,
            isSelected = true
        )
        val result = diffCallback.areItemsTheSame(firstCategoryWithCheck, newCategoryWithCheck)
        assertTrue(result)
    }

    /* areContentsTheSame */
    @Test
    fun `is one item → areContentsTheSame return true`() {
        val result = diffCallback.areContentsTheSame(firstCategoryWithCheck, firstCategoryWithCheck)
        assertTrue(result)
    }

    @Test
    fun `is different items → areContentsTheSame return false`() {
        val result =
            diffCallback.areContentsTheSame(firstCategoryWithCheck, secondCategoryWithCheck)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals id → areContentsTheSame return false`() {
        val newCategoryWithCheck = CategoryWithCheck(
            category = firstCategory,
            isSelected = true
        )
        val result = diffCallback.areContentsTheSame(firstCategoryWithCheck, newCategoryWithCheck)
        assertTrue(!result)
    }

    @Test
    fun `is different items with equals content → areContentsTheSame return true`() {
        val newCategoryWithCheck = CategoryWithCheck(
            category = firstCategory,
            isSelected = false
        )
        val result = diffCallback.areContentsTheSame(firstCategoryWithCheck, newCategoryWithCheck)
        assertTrue(result)
    }
}