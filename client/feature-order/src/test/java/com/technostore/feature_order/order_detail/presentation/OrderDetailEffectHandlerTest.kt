package com.technostore.feature_order.order_detail.presentation

import com.github.tomakehurst.wiremock.client.WireMock.exactly
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.verify
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.OrderServiceMock
import com.technostore.feature_order.business.model.OrderDetailModel
import com.technostore.feature_order.business.model.ProductOrderModel
import com.technostore.network.utils.URL
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderDetailEffectHandlerTest : OrderDetailBaseTest() {
    @get:Rule
    val wireMockRule = MockServer.testRule()

    @Before
    fun before() {
        store.setViewModel(BaseViewModel())
    }

    @Test
    fun `event init → start loading, complete orders return success  → stop loading, set order details`() =
        testScope.runTest {
            OrderServiceMock {
                getCompletedOrder.success()
            }
            val event = OrderDetailUiEvent.Init(TestData.ORDER_ID)
            orderDetailEffectHandler.process(
                event = event,
                currentState = defaultState,
                store = store
            )
            val products = listOf(
                ProductOrderModel(
                    id = TestData.FIRST_PRODUCT_ID,
                    photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
                    name = TestData.FIRST_PRODUCT_NAME,
                    rating = TestData.FIRST_PRODUCT_RATING,
                    count = TestData.FIRST_PRODUCT_COUNT,
                    price = TestData.FIRST_PRODUCT_PRICE
                )
            )
            val order = OrderDetailModel(
                id = TestData.ORDER_ID,
                products = products
            )
            coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.StartLoading) }
            verify(
                exactly(1),
                getRequestedFor(OrderServiceMock.getCompletedOrder.urlPattern)
            )
            coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.OrderDetailsLoaded(order)) }
        }

    @Test
    fun `event init → start loading, complete orders return error → show error`() =
        testScope.runTest {
            OrderServiceMock {
                getCompletedOrder.internalError()
            }
            val event = OrderDetailUiEvent.Init(TestData.ORDER_ID)
            orderDetailEffectHandler.process(
                event = event,
                currentState = defaultState,
                store = store
            )
            coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.StartLoading) }
            verify(
                exactly(1),
                getRequestedFor(OrderServiceMock.getCompletedOrder.urlPattern)
            )
            coVerify(exactly = 1) { store.acceptNews(OrderDetailNews.ShowErrorToast) }
        }

    @Test
    fun `event on Product clicked → open product page`() = testScope.runTest {
        val event = OrderDetailUiEvent.OnProductClicked(productId = TestData.FIRST_PRODUCT_ID)
        orderDetailEffectHandler.process(
            event = event,
            currentState = defaultState,
            store = store
        )
        coVerify(exactly = 1) { store.acceptNews(OrderDetailNews.OpenProductPage(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `event on back clicked → open prev page`() = testScope.runTest {
        val event = OrderDetailUiEvent.OnBackClicked
        orderDetailEffectHandler.process(
            event = event,
            currentState = defaultState,
            store = store
        )
        coVerify(exactly = 1) { store.acceptNews(OrderDetailNews.OpenPreviousPage) }
    }
}