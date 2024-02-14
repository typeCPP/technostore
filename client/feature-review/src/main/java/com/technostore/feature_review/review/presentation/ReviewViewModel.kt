package com.technostore.feature_review.review.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val store: Store<ReviewState, ReviewEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<ReviewState> = store.state

    fun init(
        userName: String,
        photoLink: String,
        date: String,
        rating: Int,
        text: String
    ) {
        viewModelScope.launch {
            store.dispatch(
                ReviewUiEvent.Init(
                    userName = userName,
                    photoLink = photoLink,
                    date = date,
                    rating = rating,
                    text = text
                )
            )
        }
    }

    fun onClickBack() {
        viewModelScope.launch {
            store.dispatch(ReviewUiEvent.OnBackClicked)
        }
    }
}