package com.technostore.feature_product.product_description.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDescriptionViewModel @Inject constructor(
    private val store: Store<ProductDescriptionState, ProductDescriptionUiEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    fun onBackClicked() {
        viewModelScope.launch {
            store.dispatch(ProductDescriptionUiEvent.OnBackClicked)
        }
    }
}
