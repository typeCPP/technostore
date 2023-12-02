package com.technostore.feature_search.search.presentation

import com.technostore.arch.mvi.State
import com.technostore.shared_search.business.model.ProductSearchModel

data class SearchState(
    val isLoading: Boolean = false,
    val products: List<ProductSearchModel> = emptyList(),
    val isEmpty: Boolean = false
) : State