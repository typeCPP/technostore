package com.technostore.feature_search.search.presentation

import com.technostore.arch.mvi.Event
import com.technostore.shared_search.business.model.ProductSearchModel

sealed class SearchEvent : Event {
    data object StartLoading : SearchEvent()
    data object EndLoading : SearchEvent()
    data class DataLoaded(val products: List<ProductSearchModel>) : SearchEvent()
    data class UpdateCount(val productId: Long, val count: Int) : SearchEvent()
    data object IsEmpty : SearchEvent()
    data object ClearData : SearchEvent()
}

sealed class SearchUiEvent : SearchEvent() {
    data class OnTextChanged(val text: String) : SearchUiEvent()
    data object OnBackClicked : SearchUiEvent()
    data class OnProductClicked(val productId: Long) : SearchUiEvent()
    data class OnPlusClicked(val productId: Long, val count: Int) : SearchUiEvent()
    data object OnFilterClicked : SearchUiEvent()
    data class LoadMoreProducts(val text: String) : SearchUiEvent()
}