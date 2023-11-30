package com.technostore.feature_shopping_cart.shopping_cart.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import com.technostore.feature_shopping_cart.business.model.ProductOrderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    initialState: ShoppingCartState,
    reducer: ShoppingCartReducer,
    effectHandler: ShoppingCartEffectHandler
) : BaseViewModel() {

    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<ShoppingCartState> = store.state

    fun init() {
        viewModelScope.launch {
            store.dispatch(ShoppingCartUiEvent.Init)
        }
    }

    fun minusClicked(productOrderModel: ProductOrderModel) {
        viewModelScope.launch {
            store.dispatch(
                ShoppingCartUiEvent.OnMinusClicked(
                    productId = productOrderModel.id,
                    count = productOrderModel.count - 1
                )
            )
        }
    }

    fun plusClicked(productOrderModel: ProductOrderModel) {
        viewModelScope.launch {
            store.dispatch(
                ShoppingCartUiEvent.OnPlusClicked(
                    productId = productOrderModel.id,
                    count = productOrderModel.count + 1
                )
            )
        }
    }

    fun removeClicked(id: Long) {
        viewModelScope.launch {
            store.dispatch(ShoppingCartUiEvent.OnRemoveClicked(id))
        }
    }

    fun setOrderClicked() {
        viewModelScope.launch {
            store.dispatch(ShoppingCartUiEvent.OnSetOrdersClicked)
        }
    }

    fun startShopping() {
        viewModelScope.launch {
            store.dispatch(ShoppingCartUiEvent.OnStartShoppingClicked)
        }
    }
}