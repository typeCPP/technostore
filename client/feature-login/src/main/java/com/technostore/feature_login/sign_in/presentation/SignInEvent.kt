package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.Event

sealed class SignInEvent : Event {
    data object EmailIsEmpty : SignInEvent()
    data object PasswordIsEmpty : SignInEvent()
    data object EmailInvalid : SignInEvent()
    data object PasswordInvalid : SignInEvent()
    data object StartLoading : SignInEvent()
    data object EndLoading : SignInEvent()
    data object EmailIsValid : SignInEvent()
    data object PasswordIsValid : SignInEvent()
}

sealed class SignInUiEvent : SignInEvent() {
    class OnSignInClicked(val email: String?, val password: String?) : SignInUiEvent()
    data object OnRegistrationClicked : SignInEvent()
    data object OnForgotPasswordClicked : SignInEvent()
}