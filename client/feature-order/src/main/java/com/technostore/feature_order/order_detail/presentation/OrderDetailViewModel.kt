package com.technostore.feature_order.order_detail.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    initialState: OrderDetailState,
    reducer: OrderDetailReducer,
    effectHandler: OrderDetailEffectHandler
) : BaseViewModel() {

    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<OrderDetailState> = store.state

    fun init(id: Long) {
        viewModelScope.launch {
            store.dispatch(OrderDetailUiEvent.Init(id))
        }
    }

    fun productClicked(id: Long) {
        viewModelScope.launch {
            store.dispatch(OrderDetailUiEvent.OnProductClicked(id))
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            store.dispatch(OrderDetailUiEvent.OnBackClicked)
        }
    }
}