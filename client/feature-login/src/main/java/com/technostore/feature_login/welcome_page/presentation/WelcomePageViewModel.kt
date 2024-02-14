package com.technostore.feature_login.welcome_page.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.State
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomePageViewModel @Inject constructor(
    private val store: Store<WelcomePageState, WelcomePageEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<State> = store.state

    fun nextClicked() {
        viewModelScope.launch {
            store.dispatch(WelcomePageEvent.OnNextClicked)
        }
    }
}