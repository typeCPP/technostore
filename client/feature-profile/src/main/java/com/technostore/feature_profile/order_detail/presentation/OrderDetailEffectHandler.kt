package com.technostore.feature_profile.order_detail.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.ProfileRepository

class OrderDetailEffectHandler(
    private val profileRepository: ProfileRepository
) : EffectHandler<OrderDetailState, OrderDetailEvent> {
    override suspend fun process(
        event: OrderDetailEvent,
        currentState: OrderDetailState,
        store: Store<OrderDetailState, OrderDetailEvent>
    ) {
        when (event) {
            is OrderDetailUiEvent.Init -> {
                store.dispatch(OrderDetailEvent.StartLoading)
                val result = profileRepository.getCompletedOrderById(event.id)
                when (result) {
                    is Result.Success -> {
                        store.dispatch(OrderDetailEvent.EndLoading)
                        store.dispatch(OrderDetailEvent.OrderDetailsLoaded(result.data!!))
                    }

                    is Result.Error -> {
                        store.acceptNews(OrderDetailNews.ShowErrorToast)
                    }
                }
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