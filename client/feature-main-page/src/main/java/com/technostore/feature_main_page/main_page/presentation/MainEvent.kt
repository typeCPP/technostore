package com.technostore.feature_main_page.main_page.presentation

import com.technostore.arch.mvi.Event
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.business.model.ProductSearchModel

sealed class MainEvent : Event {
    data object StartLoading : MainEvent()
    data object EndLoading : MainEvent()
    data class MainDataLoaded(
        val popularProducts: List<ProductSearchModel>,
        val categories: List<CategoryWithCheck>
    ) : MainEvent()

    data class DataLoaded(val products: List<ProductSearchModel>) : MainEvent()
    data class MoreDataLoaded(val products: List<ProductSearchModel>) : MainEvent()
    data class UpdateCount(val productId: Long, val count: Int) : MainEvent()
    data object IsEmpty : MainEvent()
    data object ClearData : MainEvent()
    data class SetIsMainPage(val isMainPage: Boolean) : MainEvent()
}

sealed class MainUiEvent : MainEvent() {
    data object Init : MainUiEvent()
    data class OnTextChanged(val text: String) : MainUiEvent()
    data object OnBackClicked : MainUiEvent()
    data class OnProductClicked(val productId: Long) : MainUiEvent()
    data class OnPlusClicked(val productId: Long, val count: Int) : MainUiEvent()
    data object OnFilterClicked : MainUiEvent()
    data class LoadMoreProducts(val text: String) : MainUiEvent()
    data class OnCategoryClicked(val categoryId: Long) : MainUiEvent()
    data object OnSearchCLicked : MainUiEvent()
    data object OnTextIsEmpty : MainUiEvent()
    data object MorePopularClicked : MainUiEvent()
}