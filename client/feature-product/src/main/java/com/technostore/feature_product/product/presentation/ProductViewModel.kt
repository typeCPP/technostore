package com.technostore.feature_product.product.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val store: Store<ProductState, ProductEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<ProductState> = store.state

    fun init(productId: Long) {
        viewModelScope.launch {
            store.dispatch(ProductUiEvent.Init(productId))
        }
    }

    fun onBuyClicked(productId: Long) {
        viewModelScope.launch {
            store.dispatch(ProductUiEvent.OnBuyClicked(productId))
        }
    }

    fun onRateClicked() {
        viewModelScope.launch {
            store.dispatch(ProductUiEvent.OnRateClicked)
        }
    }

    fun onMoreDescriptionClicked() {
        viewModelScope.launch {
            store.dispatch(ProductUiEvent.OnMoreDescriptionClicked)
        }
    }

    fun onMoreReviewsClicked(productId: Long) {
        viewModelScope.launch {
            store.dispatch(ProductUiEvent.OnMoreReviewClicked(productId))
        }
    }

    fun onClickBack() {
        viewModelScope.launch {
            store.dispatch(ProductUiEvent.OnBackClicked)
        }
    }

    fun setReview(rating: Int, text: String?) {
        viewModelScope.launch {
            store.dispatch(ProductUiEvent.SetReview(rating, text))
        }
    }
}