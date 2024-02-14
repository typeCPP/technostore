package com.technostore.feature_order.business

import com.technostore.arch.result.Result
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.OrderServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.feature_order.business.model.OrderDetailModel
import com.technostore.feature_order.business.model.ProductOrderModel
import com.technostore.feature_order.business.model.mapper.OrderDetailMapper
import com.technostore.feature_order.business.model.mapper.ProductOrderMapper
import com.technostore.network.model.order.response.Order
import com.technostore.network.utils.URL
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class OrderRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()
    private val productOrderMapper = ProductOrderMapper()
    private val orderDetailMapper = OrderDetailMapper(productOrderMapper)
    private val orderRepository = OrderRepositoryImpl(
        orderService = networkModule.orderService,
        orderDetailMapper = orderDetailMapper
    )
    @Test
    fun `get completed orders with 200 status → return success with data`() = runTest {
        OrderServiceMock {
            completedOrders.success()
        }
        val orders = Order(listOf(TestData.FIRST_ORDER_ID, TestData.SECOND_ORDER_ID))
        val result = orderRepository.getCompletedOrders()
        assertTrue(result is Result.Success && result.data == orders)
    }
    @Test
    fun `get completed orders with 200 status and empty body → return error`() = runTest {
        OrderServiceMock {
            completedOrders.emptyBody()
        }
        val result = orderRepository.getCompletedOrders()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `get completed orders with 500 status → return error`() = runTest {
        OrderServiceMock {
            completedOrders.internalError()
        }
        val result = orderRepository.getCompletedOrders()
        assertTrue(result is Result.Error)
    }

    /* getCompletedOrderById */
    @Test
    fun `get completed order by id with 200 status → return success with data`() = runTest {
        OrderServiceMock {
            completedOrderById.success()
        }
        val product = ProductOrderModel(
            id = TestData.FIRST_PRODUCT_ID,
            photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
            name = TestData.FIRST_PRODUCT_NAME,
            rating = TestData.FIRST_PRODUCT_RATING,
            count = TestData.FIRST_PRODUCT_COUNT,
            price = TestData.FIRST_PRODUCT_PRICE
        )
        val products = listOf(product)
        val order = OrderDetailModel(
            id = TestData.FIRST_ORDER_ID,
            products = products
        )
        val result = orderRepository.getCompletedOrderById(TestData.FIRST_ORDER_ID)
        assertTrue(result is Result.Success && result.data == order)
    }
    @Test
    fun `get completed order by id with 200 status and empty body → return error`() = runTest {
        OrderServiceMock {
            completedOrderById.emptyBody()
        }
        val result = orderRepository.getCompletedOrderById(TestData.FIRST_ORDER_ID)
        assertTrue(result is Result.Error)
    }
    @Test
    fun `get completed order by id with 500 status → return error`() = runTest {
        OrderServiceMock {
            completedOrderById.internalError()
        }
        val result = orderRepository.getCompletedOrderById(TestData.FIRST_ORDER_ID)
        assertTrue(result is Result.Error)
    }
}