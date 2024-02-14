package com.technostore.feature_order.order_detail.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_order.business.OrderRepository

class OrderDetailEffectHandler(
    private val orderRepository: OrderRepository
) : EffectHandler<OrderDetailState, OrderDetailEvent> {
    override suspend fun process(
        event: OrderDetailEvent,
        currentState: OrderDetailState,
        store: Store<OrderDetailState, OrderDetailEvent>
    ) {
        when (event) {
            is OrderDetailUiEvent.Init -> {
                store.dispatch(OrderDetailEvent.StartLoading)
                val result = orderRepository.getCompletedOrderById(event.id)
                if (result is Result.Success) {
                    val data = result.data
                    if (data != null) {
                        store.dispatch(OrderDetailEvent.EndLoading)
                        store.dispatch(OrderDetailEvent.OrderDetailsLoaded(data))
                        return
                    }
                }
                store.acceptNews(OrderDetailNews.ShowErrorToast)
            }

            is OrderDetailUiEvent.OnProductClicked -> {
                store.acceptNews(OrderDetailNews.OpenProductPage(event.productId))
            }

            is OrderDetailUiEvent.OnBackClicked -> {
                store.acceptNews(OrderDetailNews.OpenPreviousPage)
            }

            else -> {}
        }
    }
}