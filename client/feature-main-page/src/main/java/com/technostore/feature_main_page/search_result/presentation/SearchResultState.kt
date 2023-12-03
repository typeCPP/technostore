package com.technostore.feature_main_page.search_result.presentation

import com.technostore.arch.mvi.State
import com.technostore.shared_search.business.model.ProductSearchModel

data class SearchResultState(
    val isLoading: Boolean = false,
    val products: List<ProductSearchModel> = emptyList(),
) : State