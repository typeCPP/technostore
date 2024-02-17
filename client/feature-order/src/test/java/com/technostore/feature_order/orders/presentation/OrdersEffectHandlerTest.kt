package com.technostore.feature_order.orders.presentation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_order.business.OrderRepository
import com.technostore.network.model.order.response.Order
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OrdersEffectHandlerTest {
    private val order = Order(ids = listOf(1, 2, 3))
    private val orderRepository = mockk<OrderRepository>{
        coEvery { getCompletedOrders() } returns Result.Success(order)
    }
    private val effectHandler = OrdersEffectHandler(orderRepository)
    private val store = mockk<Store<OrdersState, OrdersEvent>>(relaxed = true)
    private val defaultState = OrdersState()

    /* Init */

    @Test
    fun `event init → start loading, getCompletedOrders returns Success → data!=null`() = runTest {
        val event = OrdersUiEvent.Init
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify (exactly = 1 ){ store.dispatch(OrdersEvent.StartLoading) }
        coVerify (exactly = 1 ){ orderRepository.getCompletedOrders() }
        coVerify (exactly = 1 ){ store.dispatch(OrdersEvent.OrdersLoaded(order)) }
        coVerify (exactly = 1 ){  store.dispatch(OrdersEvent.EndLoading) }
    }

    @Test
    fun `event init → start loading, getCompletedOrders returns Success → data==null`() = runTest {
        orderRepository.apply {  coEvery { getCompletedOrders() } returns Result.Success(null) }
        val event = OrdersUiEvent.Init
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify (exactly = 1 ){ store.dispatch(OrdersEvent.StartLoading) }
        coVerify (exactly = 1 ){ orderRepository.getCompletedOrders() }
        coVerify (exactly = 0 ){ store.dispatch(OrdersEvent.OrdersLoaded(order)) }
        coVerify (exactly = 0 ){  store.dispatch(OrdersEvent.EndLoading) }
        coVerify (exactly = 1 ){  store.acceptNews(OrdersNews.ShowErrorToast) }

    }

    @Test
    fun `event init → start loading, getCompletedOrders not returns Success → showError`() = runTest {
        orderRepository.apply {  coEvery { getCompletedOrders() } returns Result.Error() }
        val event = OrdersUiEvent.Init
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify (exactly = 1 ){ store.dispatch(OrdersEvent.StartLoading) }
        coVerify (exactly = 1 ){ orderRepository.getCompletedOrders() }
        coVerify (exactly = 0 ){ store.dispatch(OrdersEvent.OrdersLoaded(order)) }
        coVerify (exactly = 0 ){  store.dispatch(OrdersEvent.EndLoading) }
        coVerify (exactly = 1 ){  store.acceptNews(OrdersNews.ShowErrorToast) }
    }

    @Test
    fun `event OnBackClicked`() = runTest {
        val event = OrdersUiEvent.OnBackClicked
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify (exactly = 1 ){ store.acceptNews(OrdersNews.OpenPreviousPage) }
    }

    @Test
    fun `event OnStartShoppingClicked`() = runTest {
        val event = OrdersUiEvent.OnStartShoppingClicked
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify (exactly = 1 ){  store.acceptNews(OrdersNews.OpenMainPage) }
    }
}