package com.technostore.feature_order.orders.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val store: Store<OrdersState, OrdersEvent>
) : BaseViewModel() {
    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<OrdersState> = store.state

    fun init() {
        viewModelScope.launch {
            store.dispatch(OrdersUiEvent.Init)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            store.dispatch(OrdersUiEvent.OnBackClicked)
        }
    }

    fun orderClick(id: Long) {
        viewModelScope.launch {
            store.dispatch(OrdersUiEvent.OnOrderClicked(id))
        }
    }

    fun onGoToShoppingClicked() {
        viewModelScope.launch {
            store.dispatch(OrdersUiEvent.OnStartShoppingClicked)
        }
    }
}