package com.technostore.feature_product.product_description.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.InitialState
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDescriptionViewModel @Inject constructor(
    initialState: InitialState,
    reducer: ProductDescriptionReducer,
    effectHandler: ProductDescriptionEffectHandler
) : BaseViewModel() {

    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    fun onBackClicked() {
        viewModelScope.launch {
            store.dispatch(ProductDescriptionUiEvent.OnBackClicked)
        }
    }
}
