package com.technostore.feature_main_page.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.TestData
import com.technostore.feature_main_page.business.MainRepository
import com.technostore.feature_main_page.main_page.presentation.MainEffectHandler
import com.technostore.feature_main_page.main_page.presentation.MainState
import com.technostore.network.utils.URL
import com.technostore.shared_search.business.SharedSearchRepository
import com.technostore.shared_search.business.model.Category
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.business.model.ProductSearchModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import com.technostore.arch.result.Result
import com.technostore.feature_main_page.main_page.presentation.MainEvent
import io.mockk.just
import io.mockk.runs

open class MainPageBaseTest {
    protected val word = "word"
    val firstProductModel = ProductSearchModel(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
        price = TestData.FIRST_PRODUCT_PRICE,
        name = TestData.FIRST_PRODUCT_NAME,
        rating = TestData.FIRST_PRODUCT_RATING,
        inCartCount = TestData.FIRST_PRODUCT_COUNT,
        reviewCount = TestData.FIRST_PRODUCT_REVIEW_COUNT
    )
    private val secondProductModel = ProductSearchModel(
        id = TestData.SECOND_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.SECOND_PRODUCT_PHOTO_LINK}",
        price = TestData.SECOND_PRODUCT_PRICE,
        name = TestData.SECOND_PRODUCT_NAME,
        rating = TestData.SECOND_PRODUCT_RATING,
        inCartCount = TestData.SECOND_PRODUCT_COUNT,
        reviewCount = TestData.SECOND_PRODUCT_REVIEW_COUNT
    )
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
    val defaultProducts = listOf(firstProductModel, secondProductModel)
    val defaultCategories = listOf(firstCategoryWithCheck, secondCategoryWithCheck)

    val sharedSearchRepositoryMock = mockk<SharedSearchRepository> {
        coEvery { clearNumberPage() } just runs
        coEvery { getCategories() } returns Result.Success(defaultCategories)
        coEvery { searchProducts(any()) } returns Result.Success(defaultProducts)
        coEvery { setProductCount(any(), any()) } returns Result.Success()
    }
    val mainRepositoryMock = mockk<MainRepository> {
        coEvery { searchByPopularity() } returns Result.Success(defaultProducts)
    }
    val effectHandler = MainEffectHandler(
        sharedSearchRepository = sharedSearchRepositoryMock,
        mainRepository = mainRepositoryMock
    )
    val defaultState = MainState()
    val testScope = TestScope()
    protected val store = mockk<Store<MainState, MainEvent>>(relaxed = true)
}