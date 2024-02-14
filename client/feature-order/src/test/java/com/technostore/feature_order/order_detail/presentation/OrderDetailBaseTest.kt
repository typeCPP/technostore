package com.technostore.feature_order.order_detail.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.TestData
import com.technostore.feature_order.business.model.OrderDetailModel
import com.technostore.feature_order.business.model.ProductOrderModel
import com.technostore.network.utils.URL
import io.mockk.mockk

open class OrderDetailBaseTest {
    protected val store = mockk<Store<OrderDetailState, OrderDetailEvent>>(relaxed = true)
    protected val defaultState = OrderDetailState()
    private val products = listOf(
        ProductOrderModel(
            id = TestData.FIRST_PRODUCT_ID,
            photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
            name = TestData.FIRST_PRODUCT_NAME,
            rating = TestData.FIRST_PRODUCT_RATING,
            count = TestData.FIRST_PRODUCT_COUNT,
            price = TestData.FIRST_PRODUCT_PRICE
        )
    )
    protected val order = OrderDetailModel(
        id = TestData.FIRST_ORDER_ID,
        products = products
    )
}