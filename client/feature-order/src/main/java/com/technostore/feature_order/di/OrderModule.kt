package com.technostore.feature_order.di

import com.technostore.arch.mvi.Store
import com.technostore.feature_order.business.OrderRepository
import com.technostore.feature_order.business.OrderRepositoryImpl
import com.technostore.feature_order.business.model.mapper.OrderDetailMapper
import com.technostore.feature_order.business.model.mapper.ProductOrderMapper
import com.technostore.feature_order.order_detail.presentation.OrderDetailEffectHandler
import com.technostore.feature_order.order_detail.presentation.OrderDetailEvent
import com.technostore.feature_order.order_detail.presentation.OrderDetailReducer
import com.technostore.feature_order.order_detail.presentation.OrderDetailState
import com.technostore.feature_order.orders.presentation.OrdersEffectHandler
import com.technostore.feature_order.orders.presentation.OrdersEvent
import com.technostore.feature_order.orders.presentation.OrdersReducer
import com.technostore.feature_order.orders.presentation.OrdersState
import com.technostore.network.service.OrderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class OrderModule {
    @Provides
    fun provideProductOrderMapper(): ProductOrderMapper {
        return ProductOrderMapper()
    }

    @Provides
    fun provideOrderDetailMapper(productOrderMapper: ProductOrderMapper): OrderDetailMapper {
        return OrderDetailMapper(productOrderMapper)
    }

    @Provides
    fun provideOrderRepository(
        orderService: OrderService,
        orderDetailMapper: OrderDetailMapper
    ): OrderRepository {
        return OrderRepositoryImpl(orderService, orderDetailMapper)
    }

    /* Orders list */
    @Provides
    fun provideOrdersEffectHandler(orderRepository: OrderRepository): OrdersEffectHandler {
        return OrdersEffectHandler(orderRepository)
    }

    @Provides
    fun provideOrdersReducer(): OrdersReducer {
        return OrdersReducer()
    }

    @Provides
    fun provideOrdersState(): OrdersState {
        return OrdersState()
    }

    @Provides
    fun provideOrdersStore(
        initialState: OrdersState,
        reducer: OrdersReducer,
        effectHandler: OrdersEffectHandler
    ): Store<OrdersState, OrdersEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }

    /* Order detail */

    @Provides
    fun provideOrderDetailEffectHandler(orderRepository: OrderRepository): OrderDetailEffectHandler {
        return OrderDetailEffectHandler(orderRepository)
    }

    @Provides
    fun provideOrderDetailReducer(): OrderDetailReducer {
        return OrderDetailReducer()
    }

    @Provides
    fun provideOrderDetailState(): OrderDetailState {
        return OrderDetailState()
    }

    @Provides
    fun provideOrderDetailStore(
        initialState: OrderDetailState,
        reducer: OrderDetailReducer,
        effectHandler: OrderDetailEffectHandler
    ): Store<OrderDetailState, OrderDetailEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }
}