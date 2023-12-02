package com.technostore.feature_main_page.search_result.presentation

import com.technostore.arch.mvi.Event
import com.technostore.shared_search.business.model.ProductSearchModel

sealed class SearchResultEvent : Event {
    data object StartLoading : SearchResultEvent()
    data object EndLoading : SearchResultEvent()
    data class DataLoaded(val products: List<ProductSearchModel>) : SearchResultEvent()
    data class UpdateCount(val productId: Long, val count: Int) : SearchResultEvent()
}

sealed class SearchResultUiEvent : SearchResultEvent() {
    data class Init(val isPopularity: Boolean, val categoryId: Long?) : SearchResultUiEvent()
    data object OnBackClicked : SearchResultUiEvent()
    data class OnProductClicked(val productId: Long) : SearchResultUiEvent()
    data class OnPlusClicked(val productId: Long, val count: Int) : SearchResultUiEvent()
}