package com.technostore.feature_profile.orders.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    initialState: OrdersState,
    reducer: OrdersReducer,
    effectHandler: OrdersEffectHandler
) : BaseViewModel() {

    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
        viewModelScope.launch {
            store.dispatch(OrdersUiEvent.Init)
        }
    }

    val viewState: StateFlow<OrdersState> = store.state

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
        TODO()
    }
}