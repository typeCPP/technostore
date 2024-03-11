package com.technostore.feature_main_page.main_page.presentation

import com.technostore.arch.mvi.Reducer

class MainReducer : Reducer<MainState, MainEvent> {
    override fun reduce(currentState: MainState, event: MainEvent): MainState {
        return when (event) {
            MainEvent.StartLoading -> {
                currentState.copy(isLoading = true)
            }

            MainEvent.EndLoading -> {
                currentState.copy(isLoading = false)
            }

            is MainEvent.DataLoaded -> {
                currentState.copy(
                    productsResult = event.products,
                    isEmpty = false
                )
            }

            is MainEvent.MoreDataLoaded -> {
                currentState.copy(
                    productsResult = currentState.productsResult + event.products,
                    isEmpty = false
                )
            }

            is MainEvent.MainDataLoaded -> {
                currentState.copy(
                    popularProducts = event.popularProducts,
                    categories = event.categories
                )
            }

            is MainEvent.UpdateCount -> {
                currentState.productsResult.forEach {
                    if (it.id == event.productId) {
                        it.inCartCount = event.count
                    }
                }
                return currentState
            }

            is MainEvent.IsEmpty -> {
                currentState.copy(
                    isLoading = false,
                    productsResult = emptyList(),
                    isEmpty = true
                )
            }

            is MainEvent.ClearData -> {
                currentState.copy(
                    isMainPage = true,
                    productsResult = emptyList()
                )
            }

            is MainEvent.SetIsMainPage -> {
                currentState.copy(
                    isMainPage = event.isMainPage
                )
            }

            else -> currentState
        }
    }
}
