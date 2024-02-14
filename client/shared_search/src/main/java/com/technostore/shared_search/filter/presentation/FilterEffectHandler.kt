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
                    is Result.Success -> {
                        val categories = result.data
                        if (categories != null) {
                            val selectedCategories = sharedSearchRepository.getSelectedCategories()
                            categories.forEach { categoryWithCheck ->
                                if (selectedCategories.find { categoryWithCheck.category.id == it } != null) {
                                    categoryWithCheck.isSelected = true
                                }
                            }
                            store.dispatch(
                                FilterEvent.DataLoaded(
                                    categories = categories,
                                    isSortingByPopularity = sharedSearchRepository.getIsSortByPopularity(),
                                    isSortingByRating = sharedSearchRepository.getIsSortByRating(),
                                    minPrice = sharedSearchRepository.getMinPrice(),
                                    maxPrice = sharedSearchRepository.getMaxPrice(),
                                    minRating = sharedSearchRepository.getMinRating(),
                                    maxRating = sharedSearchRepository.getMaxRating(),
                                )
                            )
                        } else {
                            store.acceptNews(FilterNews.ShowErrorToast)
                        }
                    }
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
                if (isSelected) {
                    store.acceptNews(FilterNews.ChangeIsSortingByRatingBackground(false))
                }
                store.acceptNews(FilterNews.ChangeIsSortingByPopularityBackground(isSelected))
            }

            FilterUiEvent.OnChangedIsSortingByRating -> {
                val isSelected = sharedSearchRepository.setIsSelectByRating()
                if (isSelected) {
                    store.acceptNews(FilterNews.ChangeIsSortingByPopularityBackground(false))
                }
                store.acceptNews(FilterNews.ChangeIsSortingByRatingBackground(isSelected))
            }

            FilterUiEvent.OnNextButtonClicked, FilterUiEvent.OnBackButtonClicked -> {
                store.acceptNews(FilterNews.OpenPreviousPage)
            }

            else -> {}
        }
    }
}