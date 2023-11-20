package com.technostore.feature_login.registration_user_info.presentation

import android.net.Uri
import com.technostore.arch.mvi.Event

sealed class RegistrationUserInfoEvent : Event {
    data object NameIsValid : RegistrationUserInfoEvent()
    data object NameIsEmpty : RegistrationUserInfoEvent()
    data object NameErrorLength : RegistrationUserInfoEvent()

    data object LastNameIsValid : RegistrationUserInfoEvent()
    data object LastNameIsEmpty : RegistrationUserInfoEvent()
    data object LastNameErrorLength : RegistrationUserInfoEvent()

    data object StartLoading : RegistrationUserInfoEvent()
    data object EndLoading : RegistrationUserInfoEvent()
}

sealed class RegistrationUserInfoUiEvent : RegistrationUserInfoEvent() {
    class OnImageChanged(val uri: Uri?) : RegistrationUserInfoUiEvent()
    class OnRegistrationClicked(
        val byteArray: ByteArray?,
        val email: String,
        val password: String,
        val name: String,
        val lastName: String,
    ) : RegistrationUserInfoUiEvent()
}