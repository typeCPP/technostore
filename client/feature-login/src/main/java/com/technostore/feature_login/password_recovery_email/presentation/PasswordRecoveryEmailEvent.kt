package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.arch.mvi.Event

sealed class PasswordRecoveryEmailEvent : Event {
    data object EmailIsEmpty : PasswordRecoveryEmailEvent()
    data object EmailMaxLength : PasswordRecoveryEmailEvent()
    data object EmailIsInvalid : PasswordRecoveryEmailEvent()
    data object EmailNotExists : PasswordRecoveryEmailEvent()
    data object EmailIsValid : PasswordRecoveryEmailEvent()

    data object StartLoading : PasswordRecoveryEmailEvent()
    data object EndLoading : PasswordRecoveryEmailEvent()
}

sealed class PasswordRecoveryEmailUIEvent : PasswordRecoveryEmailEvent() {
    data class OnNextClicked(val email: String) : PasswordRecoveryEmailUIEvent()
}