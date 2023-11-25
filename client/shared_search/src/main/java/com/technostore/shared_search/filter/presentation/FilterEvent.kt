package com.technostore.shared_search.filter.presentation

import com.technostore.arch.mvi.Event
import com.technostore.shared_search.business.model.CategoryWithCheck

sealed class FilterEvent : Event {
    data object StartLoading : FilterEvent()
    data object EndLoading : FilterEvent()
    class DataLoaded(val categories: List<CategoryWithCheck>) : FilterUiEvent()
}

sealed class FilterUiEvent : FilterEvent() {
    data object Init : FilterUiEvent()
    class OnClickCategory(val categoryWithCheck: CategoryWithCheck) : FilterUiEvent()
    class OnChangedRatingBoundaries(val minValue: Float, val maxValue: Float) : FilterUiEvent()
    class OnChangedCostBoundaries(val minValue: Float, val maxValue: Float) : FilterUiEvent()
    data object OnNextButtonClicked : FilterUiEvent()
    data object OnBackButtonClicked : FilterUiEvent()
    data object OnChangedIsSortingByPopularity : FilterUiEvent()
    data object OnChangedIsSortingByRating : FilterUiEvent()
}