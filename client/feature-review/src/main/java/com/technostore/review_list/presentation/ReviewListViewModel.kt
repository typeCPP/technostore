package com.technostore.review_list.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import com.technostore.business.model.ReviewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewListViewModel @Inject constructor(
    private val store: Store<ReviewListState, ReviewListEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<ReviewListState> = store.state

    fun init(productId: Long) {
        viewModelScope.launch {
            store.dispatch(ReviewListUiEvent.Init(productId))
        }
    }

    fun onClickBack() {
        viewModelScope.launch {
            store.dispatch(ReviewListUiEvent.OnBackClicked)
        }
    }

    fun onReviewClicked(reviewModel: ReviewModel) {
        viewModelScope.launch {
            store.dispatch(ReviewListUiEvent.OnReviewClicked(reviewModel))
        }
    }

    fun onAllReviewsClicked() {
        viewModelScope.launch {
            store.dispatch(ReviewListUiEvent.OnAllReviewsClicked)
        }
    }

    fun onPositiveReviewsClicked() {
        viewModelScope.launch {
            store.dispatch(ReviewListUiEvent.OnPositiveReviewsClicked)
        }
    }

    fun onNegativeReviewsClicked() {
        viewModelScope.launch {
            store.dispatch(ReviewListUiEvent.OnNegativeReviewsClicked)
        }
    }

    fun onNeutralReviewsClicked() {
        viewModelScope.launch {
            store.dispatch(ReviewListUiEvent.OnNeutralReviewsClicked)
        }
    }
}