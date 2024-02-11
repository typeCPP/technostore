package com.technostore.feature_order.order_detail.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.feature_order.business.OrderRepositoryImpl
import com.technostore.feature_order.business.model.mapper.OrderDetailMapper
import com.technostore.feature_order.business.model.mapper.ProductOrderMapper
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope

open class OrderDetailBaseTest {
    protected val store = mockk<Store<OrderDetailState, OrderDetailEvent>>(relaxed = true)
    private val networkModule = NetworkModuleTest()
    protected val orderDetailReducer = OrderDetailReducer()
    protected val defaultState = OrderDetailState()
    private val productOrderMapper = ProductOrderMapper()
    private val orderDetailMapper = OrderDetailMapper(productOrderMapper)

    private val orderRepository = OrderRepositoryImpl(
        orderService = networkModule.orderService,
        orderDetailMapper = orderDetailMapper
    )
    protected val orderDetailEffectHandler = OrderDetailEffectHandler(
        orderRepository = orderRepository
    )
    protected val testScope = TestScope()
}