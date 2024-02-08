package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.common_test.TestData
import com.technostore.feature_shopping_cart.business.model.OrderDetailModel
import com.technostore.feature_shopping_cart.business.model.ProductOrderModel
import com.technostore.network.utils.URL

open class ShoppingCartBaseTest {
    protected val firstProduct = ProductOrderModel(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
        name = TestData.FIRST_PRODUCT_NAME,
        rating = TestData.FIRST_PRODUCT_RATING,
        count = TestData.FIRST_PRODUCT_COUNT,
        price = TestData.FIRST_PRODUCT_PRICE
    )
    protected val secondProduct = ProductOrderModel(
        id = TestData.SECOND_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.SECOND_PRODUCT_PHOTO_LINK}",
        name = TestData.SECOND_PRODUCT_NAME,
        rating = TestData.SECOND_PRODUCT_RATING,
        count = TestData.SECOND_PRODUCT_COUNT,
        price = TestData.SECOND_PRODUCT_PRICE
    )
    protected val order = OrderDetailModel(
        id = TestData.ORDER_ID,
        products = listOf(firstProduct)
    )
    protected val defaultState = ShoppingCartState()
}