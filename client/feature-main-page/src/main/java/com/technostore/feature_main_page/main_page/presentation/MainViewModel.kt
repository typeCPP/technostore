package com.technostore.feature_main_page.main_page.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import com.technostore.shared_search.business.model.ProductSearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    initialState: MainState,
    reducer: MainReducer,
    effectHandler: MainEffectHandler
) : BaseViewModel() {

    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<MainState> = store.state

    fun init() {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.Init)
        }
    }

    fun search(text: String) {
        viewModelScope.launch {
            store.dispatch(
                MainUiEvent.OnTextChanged(text)
            )
        }
    }

    fun plusClicked(productOrderModel: ProductSearchModel) {
        viewModelScope.launch {
            val count = productOrderModel.inCartCount + 1

            store.dispatch(
                MainUiEvent.OnPlusClicked(
                    productId = productOrderModel.id,
                    count = count
                )
            )
        }
    }

    fun onFilterClicked() {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.OnFilterClicked)
        }
    }

    fun onProductClicked(productId: Long) {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.OnProductClicked(productId))
        }
    }

    fun loadMoreProducts(text: String) {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.LoadMoreProducts(text))
        }
    }

    fun onBackButtonClicked() {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.OnBackClicked)
        }
    }

    fun onClickCategory(categoryId: Long) {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.OnCategoryClicked(categoryId))
        }
    }

    fun onClickSearch() {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.OnSearchCLicked)
        }
    }

    fun textIsEmpty() {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.OnTextIsEmpty)
        }
    }

    fun morePopularityClicked() {
        viewModelScope.launch {
            store.dispatch(MainUiEvent.MorePopularClicked)
        }
    }
}