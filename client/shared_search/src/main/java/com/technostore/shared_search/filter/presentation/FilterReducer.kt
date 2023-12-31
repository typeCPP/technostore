package com.technostore.shared_search.filter.presentation

import com.technostore.arch.mvi.Reducer

class FilterReducer : Reducer<FilterState, FilterEvent> {
    override fun reduce(currentState: FilterState, event: FilterEvent): FilterState {
        return when (event) {

            FilterEvent.StartLoading -> currentState.copy(
                isLoading = true
            )

            FilterEvent.EndLoading -> currentState.copy(
                isLoading = false
            )

            is FilterEvent.DataLoaded -> currentState.copy(
                categories = event.categories,
                isSortingByPopularity = event.isSortingByPopularity,
                isSortingByRating = event.isSortingByRating,
                minPrice = event.minPrice,
                maxPrice = event.maxPrice,
                minRating = event.minRating,
                maxRating = event.maxRating
            )

            else -> currentState
        }
    }
}