package com.technostore.feature_profile.profile.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val store: Store<ProfileState, ProfileEvent>
) : BaseViewModel() {

    val viewState: StateFlow<ProfileState> = store.state

    init {
        store.setViewModel(this)
    }

    fun init() {
        viewModelScope.launch {
            store.dispatch(ProfileUiEvent.Init)
        }
    }

    fun editPasswordClicked() {
        viewModelScope.launch {
            store.dispatch(ProfileUiEvent.OnChangePasswordClicked)
        }
    }

    fun editProfileClicked() {
        viewModelScope.launch {
            store.dispatch(ProfileUiEvent.OnChangeProfileClicked)
        }
    }

    fun logoutClicked() {
        viewModelScope.launch {
            store.dispatch(ProfileUiEvent.OnLogoutClicked)
        }
    }

    fun orderHistoryClicked() {
        viewModelScope.launch {
            store.dispatch(ProfileUiEvent.OnOrderHistoryClicked)
        }
    }
}