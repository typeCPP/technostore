package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.Event

sealed class SignInEvent : Event {
    object EmailIsEmpty : SignInEvent()
    object PasswordIsEmpty : SignInEvent()
    object EmailInvalid : SignInEvent()
    object PasswordInvalid : SignInEvent()
    object StartLoading : SignInEvent()
    object EndLoading : SignInEvent()
    object EmailIsValid : SignInEvent()
    object PasswordIsValid : SignInEvent()
}

sealed class SignInUiEvent : SignInEvent() {
    class OnSignInClicked(val email: String?, val password: String?) : SignInUiEvent()
    object OnRegistrationClicked : SignInEvent()
    object OnForgotPasswordClicked : SignInEvent()
}