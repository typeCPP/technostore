package com.technostore.shared_search.filter.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.shared_search.business.SharedSearchRepository

class FilterEffectHandler(
    private val sharedSearchRepository: SharedSearchRepository
) : EffectHandler<FilterState, FilterEvent> {
    override suspend fun process(
        event: FilterEvent,
        currentState: FilterState,
        store: Store<FilterState, FilterEvent>
    ) {
        when (event) {
            FilterUiEvent.Init -> {
                store.dispatch(FilterEvent.StartLoading)
                val result = sharedSearchRepository.getCategories()
                store.dispatch(FilterEvent.EndLoading)
                when (result) {
                    is Result.Error -> store.acceptNews(FilterNews.ShowErrorToast)
                    is Result.Success -> store.dispatch(FilterEvent.DataLoaded(result.data!!))
                }
            }

            is FilterUiEvent.OnClickCategory -> {
                sharedSearchRepository.updateSelectedCategories(event.categoryWithCheck)
            }

            is FilterUiEvent.OnChangedRatingBoundaries -> {
                sharedSearchRepository.updateRatingBoundaries(
                    minRating = event.minValue,
                    maxRating = event.maxValue
                )
            }

            is FilterUiEvent.OnChangedCostBoundaries -> {
                sharedSearchRepository.updateCostBoundaries(
                    minCost = event.minValue,
                    maxCost = event.maxValue
                )
            }

            FilterUiEvent.OnChangedIsSortingByPopularity -> {
                val isSelected = sharedSearchRepository.setIsSelectByPopularity()
                store.acceptNews(FilterNews.ChangeIsSortingByPopularityBackground(isSelected))
            }

            FilterUiEvent.OnChangedIsSortingByRating -> {
                val isSelected = sharedSearchRepository.setIsSelectByRating()
                store.acceptNews(FilterNews.ChangeIsSortingByRatingBackground(isSelected))
            }

            FilterUiEvent.OnNextButtonClicked, FilterUiEvent.OnNextButtonClicked -> {
                store.acceptNews(FilterNews.OpenPreviousPage)
            }

            else -> {}
        }
    }
}