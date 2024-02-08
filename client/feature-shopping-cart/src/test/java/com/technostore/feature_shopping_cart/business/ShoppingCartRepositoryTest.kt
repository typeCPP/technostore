package com.technostore.feature_shopping_cart.business

import com.technostore.arch.result.Result
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData.ORDER_ID
import com.technostore.common_test.TestData.FIRST_PRODUCT_COUNT
import com.technostore.common_test.TestData.FIRST_PRODUCT_ID
import com.technostore.common_test.TestData.FIRST_PRODUCT_NAME
import com.technostore.common_test.TestData.FIRST_PRODUCT_PHOTO_LINK
import com.technostore.common_test.TestData.FIRST_PRODUCT_PRICE
import com.technostore.common_test.TestData.FIRST_PRODUCT_RATING
import com.technostore.common_test.mock.OrderServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.feature_shopping_cart.business.ShoppingCartRepositoryImpl
import com.technostore.feature_shopping_cart.business.model.OrderDetailModel
import com.technostore.feature_shopping_cart.business.model.ProductOrderModel
import com.technostore.feature_shopping_cart.business.model.mapper.OrderDetailMapper
import com.technostore.feature_shopping_cart.business.model.mapper.ProductOrderMapper
import com.technostore.network.utils.URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ShoppingCartRepositoryTest {

    private val networkModule = NetworkModuleTest()

    private val productOrderMapper = ProductOrderMapper()
    private val orderDetailMapper = OrderDetailMapper(productOrderMapper)

    @get:Rule
    val wireMockRule = MockServer.testRule()
    private val shoppingCartRepository = ShoppingCartRepositoryImpl(
        orderService = networkModule.orderService,
        orderDetailMapper = orderDetailMapper
    )

    private val testScope = TestScope()


    /* get current order */
    @Test
    fun `get current order with 200 status → return success with order model`() =
        testScope.runTest {
            OrderServiceMock {
                getCurrentOrder.success()
            }
            val expectedProducts = listOf(
                ProductOrderModel(
                    id = FIRST_PRODUCT_ID,
                    photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}$FIRST_PRODUCT_PHOTO_LINK",
                    name = FIRST_PRODUCT_NAME,
                    rating = FIRST_PRODUCT_RATING,
                    count = FIRST_PRODUCT_COUNT,
                    price = FIRST_PRODUCT_PRICE
                )
            )
            val expectedOrder = OrderDetailModel(
                id = ORDER_ID,
                products = expectedProducts
            )
            val result = shoppingCartRepository.getCurrentOrder()
            assertTrue(result is Result.Success && result.data == expectedOrder)
        }

    @Test
    fun `get current order with 500 status → return error`() = testScope.runTest {
        OrderServiceMock {
            getCurrentOrder.internalError()
        }
        val result = shoppingCartRepository.getCurrentOrder()
        assertTrue(result is Result.Error)
    }

    /* make order completed */
    @Test
    fun `make order completed with 200 status → return success`() = testScope.runTest {
        OrderServiceMock {
            completeOrder.success()
        }
        val result = shoppingCartRepository.makeOrderCompleted(ORDER_ID)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `make order completed with 500 status → return error`() = testScope.runTest {
        OrderServiceMock {
            completeOrder.internalError()
        }
        val result = shoppingCartRepository.makeOrderCompleted(ORDER_ID)
        assertTrue(result is Result.Error)
    }

    /* setProductCount */
    @Test
    fun `set product count with 200 status → return success`() = testScope.runTest {
        OrderServiceMock {
            setProductCount.success()
        }
        val result = shoppingCartRepository.setProductCount(FIRST_PRODUCT_ID, FIRST_PRODUCT_COUNT)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `set product count with 500 status → return error`() = testScope.runTest {
        OrderServiceMock {
            setProductCount.internalError()
        }
        val result = shoppingCartRepository.setProductCount(FIRST_PRODUCT_ID, FIRST_PRODUCT_COUNT)
        assertTrue(result is Result.Error)
    }

}