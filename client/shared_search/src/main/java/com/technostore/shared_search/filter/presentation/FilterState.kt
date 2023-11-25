package com.technostore.shared_search.filter.presentation

import com.technostore.arch.mvi.State
import com.technostore.shared_search.business.model.CategoryWithCheck

data class FilterState(
    val isLoading: Boolean = false,
    val categories:List<CategoryWithCheck> = listOf()
) : State