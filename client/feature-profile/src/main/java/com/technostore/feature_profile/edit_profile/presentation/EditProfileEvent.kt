package com.technostore.feature_profile.edit_profile.presentation

import com.technostore.arch.mvi.Event

sealed class EditProfileEvent : Event {
    data object StartLoading : EditProfileEvent()
    data object EndLoading : EditProfileEvent()
}

sealed class EditProfileUiEvent : EditProfileEvent() {
    data class OnChangeProfileClicked(
        val byteArray: ByteArray?,
        val email: String,
        val password: String,
        val name: String,
        val lastName: String,
    ) : EditProfileUiEvent()

    data object OnBackButtonClicked : EditProfileUiEvent()
}