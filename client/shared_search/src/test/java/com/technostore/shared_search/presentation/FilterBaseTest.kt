package com.technostore.shared_search.presentation

import com.technostore.common_test.TestData
import com.technostore.shared_search.business.model.Category
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.filter.presentation.FilterState

open class FilterBaseTest {
    protected val defaultState = FilterState()
    private val firstCategory = Category(
        id = TestData.FIRST_CATEGORY_ID,
        name = TestData.FIRST_CATEGORY_NAME
    )
    private val secondCategory = Category(
        id = TestData.SECOND_CATEGORY_ID,
        name = TestData.SECOND_CATEGORY_NAME
    )
    protected val firstCategoryWithCheck = CategoryWithCheck(
        category = firstCategory,
        isSelected = false
    )
    protected val secondCategoryWithCheck = CategoryWithCheck(
        category = secondCategory,
        isSelected = false
    )
    protected val categoryWithCheckList = listOf(firstCategoryWithCheck, secondCategoryWithCheck)
    protected val defaultMinPrice = 0f
    protected val defaultMaxPrice = 100000f
    protected val defaultMinRating = 0f
    protected val defaultMaxRating = 10f
    protected val selectedCategories = arrayListOf(TestData.FIRST_CATEGORY_ID)
}