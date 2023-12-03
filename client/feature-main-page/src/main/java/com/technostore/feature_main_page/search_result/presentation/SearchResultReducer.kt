package com.technostore.feature_main_page.search_result.presentation

import com.technostore.arch.mvi.Reducer

class SearchResultReducer : Reducer<SearchResultState, SearchResultEvent> {
    override fun reduce(
        currentState: SearchResultState,
        event: SearchResultEvent
    ): SearchResultState {
        return when (event) {
            SearchResultEvent.StartLoading -> {
                currentState.copy(isLoading = true)
            }

            SearchResultEvent.EndLoading -> {
                currentState.copy(isLoading = false)
            }

            is SearchResultEvent.DataLoaded -> {
                currentState.copy(
                    products = event.products,
                )
            }

            is SearchResultEvent.UpdateCount -> {
                currentState.products.forEach {
                    if (it.id == event.productId) {
                        it.inCartCount = event.count
                    }
                }
                return currentState
            }

            else -> currentState
        }
    }
}