package com.technostore.feature_search.search.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import com.technostore.shared_search.business.model.ProductSearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    initialState: SearchState,
    reducer: SearchReducer,
    effectHandler: SearchEffectHandler
) : BaseViewModel() {

    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<SearchState> = store.state

    fun init() {
        viewModelScope.launch {
            store.dispatch(SearchUiEvent.Init)
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            store.dispatch(
                SearchUiEvent.OnTextChanged(text)
            )
        }
    }

    fun plusClicked(productOrderModel: ProductSearchModel) {
        viewModelScope.launch {
            val count = productOrderModel.inCartCount + 1

            store.dispatch(
                SearchUiEvent.OnPlusClicked(
                    productId = productOrderModel.id,
                    count = count
                )
            )
        }
    }

    fun onFilterClicked() {
        viewModelScope.launch {
            store.dispatch(SearchUiEvent.OnFilterClicked)
        }
    }

    fun onProductClicked(productId: Long) {
        viewModelScope.launch {
            store.dispatch(SearchUiEvent.OnProductClicked(productId))
        }
    }

    fun loadMoreProducts(text: String) {
        viewModelScope.launch {
            store.dispatch(SearchUiEvent.LoadMoreProducts(text))
        }
    }

    fun onBackButtonClicked() {
        viewModelScope.launch {
            store.dispatch(SearchUiEvent.OnBackClicked)
        }
    }
}