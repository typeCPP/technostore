package com.technostore.feature_profile.change_password.presentation

import com.technostore.arch.mvi.Event

sealed class ChangePasswordEvent : Event {
    data object StartLoading : ChangePasswordEvent()
    data object EndLoading : ChangePasswordEvent()
}

sealed class ChangePasswordUiEvent : ChangePasswordEvent() {
    data class OnChangePasswordClicked(
        val oldPassword: String,
        val newPassword: String,
        val newRepeatPassword: String
    ) : ChangePasswordUiEvent()

    data object OnBackButtonClicked : ChangePasswordUiEvent()
}