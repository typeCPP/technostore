package com.technostore.feature_main_page.main_page.presentation

import com.technostore.arch.mvi.State
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.business.model.ProductSearchModel

data class MainState(
    val isLoading: Boolean = false,
    val productsResult: List<ProductSearchModel> = emptyList(),
    val popularProducts: List<ProductSearchModel> = emptyList(),
    val categories: List<CategoryWithCheck> = emptyList(),
    val isEmpty: Boolean = false,
    val isMainPage: Boolean = true
) : State