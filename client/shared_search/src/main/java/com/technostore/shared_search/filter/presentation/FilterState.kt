package com.technostore.shared_search.filter.presentation

import com.technostore.arch.mvi.State
import com.technostore.shared_search.business.model.CategoryWithCheck

data class FilterState(
    val isLoading: Boolean = false,
    val categories: List<CategoryWithCheck> = listOf(),
    val isSortingByPopularity: Boolean = false,
    val isSortingByRating: Boolean = false,
    val minPrice: Float = 0f,
    val maxPrice: Float = 1000000f,
    val minRating: Float = 0f,
    val maxRating: Float = 10f
) : State