package com.technostore.feature_login.registration.presentation

import com.technostore.arch.mvi.Event

sealed class RegistrationEvent : Event {
    data object EmailIsEmpty : RegistrationEvent()
    data object EmailMaxLength : RegistrationEvent()
    data object EmailIsInvalid : RegistrationEvent()
    data object EmailExists : RegistrationEvent()
    data object EmailIsValid : RegistrationEvent()

    data object FirstPasswordIsEmpty : RegistrationEvent()
    data object FirstPasswordErrorSymbols : RegistrationEvent()
    data object FirstPasswordErrorMinLength : RegistrationEvent()
    data object FirstPasswordErrorMaxLength : RegistrationEvent()
    data object FirstPasswordIsValid : RegistrationEvent()

    data object SecondPasswordIsEmpty : RegistrationEvent()
    data object SecondPasswordErrorSymbols : RegistrationEvent()
    data object SecondPasswordErrorMinLength : RegistrationEvent()
    data object SecondPasswordErrorMaxLength : RegistrationEvent()
    data object SecondPasswordIsValid : RegistrationEvent()

    data object PasswordsAreBroken : RegistrationEvent()

    data object StartLoading : RegistrationEvent()
    data object EndLoading : RegistrationEvent()
}


sealed class RegistrationUiEvent : RegistrationEvent() {
    data class OnRegistrationClicked(
        val email: String,
        val firstPassword: String,
        val secondPassword: String
    ) : RegistrationUiEvent()

    data object OnLoginClicked : RegistrationUiEvent()
}