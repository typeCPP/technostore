package com.technostore.feature_search.search.presentation

import com.technostore.arch.mvi.Reducer

class SearchReducer : Reducer<SearchState, SearchEvent> {
    override fun reduce(currentState: SearchState, event: SearchEvent): SearchState {
        return when (event) {
            SearchEvent.StartLoading -> {
                currentState.copy(isLoading = true)
            }

            SearchEvent.EndLoading -> {
                currentState.copy(isLoading = false)
            }

            is SearchEvent.DataLoaded -> {
                currentState.copy(
                    products = event.products,
                    isEmpty = false
                )
            }

            is SearchEvent.UpdateCount -> {
                currentState.products.forEach {
                    if (it.id == event.productId) {
                        it.inCartCount = event.count
                    }
                }
                return currentState
            }

            is SearchEvent.IsEmpty -> {
                currentState.copy(
                    isLoading = false,
                    products = emptyList(),
                    isEmpty = true
                )
            }

            is SearchEvent.ClearData -> {
                currentState.copy(
                    products = emptyList()
                )
            }

            else -> currentState
        }
    }

}