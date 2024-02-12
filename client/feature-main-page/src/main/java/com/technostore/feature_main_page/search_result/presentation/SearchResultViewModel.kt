package com.technostore.feature_main_page.search_result.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import com.technostore.shared_search.business.model.ProductSearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val store: Store<SearchResultState, SearchResultEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<SearchResultState> = store.state

    fun init(isPopularity: Boolean, categoryId: Long?) {
        viewModelScope.launch {
            store.dispatch(
                SearchResultUiEvent.Init(
                    isPopularity = isPopularity,
                    categoryId = categoryId
                )
            )
        }
    }

    fun plusClicked(productOrderModel: ProductSearchModel) {
        viewModelScope.launch {
            val count = productOrderModel.inCartCount + 1
            store.dispatch(
                SearchResultUiEvent.OnPlusClicked(
                    productId = productOrderModel.id,
                    count = count
                )
            )
        }
    }

    fun onProductClicked(productId: Long) {
        viewModelScope.launch {
            store.dispatch(SearchResultUiEvent.OnProductClicked(productId))
        }
    }

    fun onBackButtonClicked() {
        viewModelScope.launch {
            store.dispatch(SearchResultUiEvent.OnBackClicked)
        }
    }
}