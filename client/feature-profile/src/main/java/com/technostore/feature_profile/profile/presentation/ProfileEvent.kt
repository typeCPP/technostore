package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.mvi.Event

sealed class ProfileEvent : Event {
    data object StartLoading : ProfileEvent()
    data object EndLoading : ProfileEvent()
    data class ProfileLoaded(
        val name: String,
        val lastName: String,
        val email: String,
        val image: String,
    ) : ProfileEvent()
}

sealed class ProfileUiEvent : ProfileEvent() {
    data object Init : ProfileEvent()
    data object OnLogoutClicked : ProfileEvent()
    data object OnChangePasswordClicked : ProfileEvent()
    data object OnChangeProfileClicked : ProfileEvent()
    data object OnOrderHistoryClicked : ProfileEvent()
}