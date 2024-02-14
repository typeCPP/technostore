package com.technostore.feature_order.order_detail.presentation

import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_order.business.OrderRepository
import com.technostore.network.model.order.response.Order
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OrderDetailEffectHandlerTest : OrderDetailBaseTest() {
    private val orders = Order(listOf(TestData.FIRST_ORDER_ID, TestData.SECOND_ORDER_ID))
    private val orderRepository = mockk<OrderRepository>() {
        coEvery { getCompletedOrders() } returns Result.Success(orders)
        coEvery { getCompletedOrderById(any()) } returns Result.Success(order)
    }
    private val orderDetailEffectHandler = OrderDetailEffectHandler(
        orderRepository = orderRepository
    )

    @Test
    fun `event init → start loading, complete orders return success → stop loading, set order details`() =
        runTest {
            val event = OrderDetailUiEvent.Init(TestData.FIRST_ORDER_ID)
            orderDetailEffectHandler.process(
                event = event,
                currentState = defaultState,
                store = store
            )
            coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.StartLoading) }
            coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.OrderDetailsLoaded(order)) }
        }

    @Test
    fun `event init → start loading, complete orders return error → show error`() = runTest {
        orderRepository.apply {
            coEvery { getCompletedOrderById(any()) } returns Result.Error()
        }
        val event = OrderDetailUiEvent.Init(TestData.FIRST_ORDER_ID)
        orderDetailEffectHandler.process(
            event = event,
            currentState = defaultState,
            store = store
        )
        coVerify(exactly = 1) { store.dispatch(OrderDetailEvent.StartLoading) }
        coVerify(exactly = 1) { store.acceptNews(OrderDetailNews.ShowErrorToast) }
    }

    @Test
    fun `event on Product clicked → open product page`() = runTest {
        val event = OrderDetailUiEvent.OnProductClicked(productId = TestData.FIRST_PRODUCT_ID)
        orderDetailEffectHandler.process(
            event = event,
            currentState = defaultState,
            store = store
        )
        coVerify(exactly = 1) { store.acceptNews(OrderDetailNews.OpenProductPage(TestData.FIRST_PRODUCT_ID)) }
    }

    @Test
    fun `event on back clicked → open prev page`() = runTest {
        val event = OrderDetailUiEvent.OnBackClicked
        orderDetailEffectHandler.process(
            event = event,
            currentState = defaultState,
            store = store
        )
        coVerify(exactly = 1) { store.acceptNews(OrderDetailNews.OpenPreviousPage) }
    }
}