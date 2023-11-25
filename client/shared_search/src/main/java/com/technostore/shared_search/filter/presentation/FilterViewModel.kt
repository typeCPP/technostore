package com.technostore.shared_search.filter.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import com.technostore.shared_search.business.model.CategoryWithCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    initialState: FilterState,
    reducer: FilterReducer,
    effectHandler: FilterEffectHandler
) : BaseViewModel() {
    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<FilterState> = store.state

    fun init() {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.Init)
        }
    }

    fun onNextButtonClicked() {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.OnNextButtonClicked)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.OnBackButtonClicked)
        }
    }

    fun onClickCategory(categoryWithCheck: CategoryWithCheck) {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.OnClickCategory(categoryWithCheck = categoryWithCheck))
        }
    }

    fun changeRatingBoundaries(minValue: Float, maxValue: Float) {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.OnChangedRatingBoundaries(minValue, maxValue))
        }
    }

    fun changeCostBoundaries(minValue: Float, maxValue: Float) {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.OnChangedCostBoundaries(minValue, maxValue))
        }
    }

    fun changeIsSortingByPopularity() {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.OnChangedIsSortingByPopularity)
        }
    }

    fun changeIsSortingByRating() {
        viewModelScope.launch {
            store.dispatch(FilterUiEvent.OnChangedIsSortingByRating)
        }
    }
}