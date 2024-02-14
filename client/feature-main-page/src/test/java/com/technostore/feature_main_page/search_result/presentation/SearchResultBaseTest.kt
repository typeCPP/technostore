package com.technostore.feature_main_page.search_result.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.TestData
import com.technostore.network.utils.URL
import com.technostore.shared_search.business.model.ProductSearchModel
import io.mockk.mockk

open class SearchResultBaseTest {
    protected val firstProductModel = ProductSearchModel(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
        price = TestData.FIRST_PRODUCT_PRICE,
        name = TestData.FIRST_PRODUCT_NAME,
        rating = TestData.FIRST_PRODUCT_RATING,
        inCartCount = TestData.FIRST_PRODUCT_COUNT,
        reviewCount = TestData.FIRST_PRODUCT_REVIEW_COUNT
    )
    protected val secondProductModel = ProductSearchModel(
        id = TestData.SECOND_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.SECOND_PRODUCT_PHOTO_LINK}",
        price = TestData.SECOND_PRODUCT_PRICE,
        name = TestData.SECOND_PRODUCT_NAME,
        rating = TestData.SECOND_PRODUCT_RATING,
        inCartCount = TestData.SECOND_PRODUCT_COUNT,
        reviewCount = TestData.SECOND_PRODUCT_REVIEW_COUNT
    )
    protected val defaultProducts = listOf(firstProductModel, secondProductModel)
    protected val store = mockk<Store<SearchResultState, SearchResultEvent>>(relaxed = true)
}