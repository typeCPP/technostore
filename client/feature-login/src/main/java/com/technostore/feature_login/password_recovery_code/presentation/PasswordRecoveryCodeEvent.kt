package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.arch.mvi.Event

sealed class PasswordRecoveryCodeEvent : Event {
    data object StartLoading : PasswordRecoveryCodeEvent()
    data object EndLoading : PasswordRecoveryCodeEvent()
}

sealed class PasswordRecoveryCodeUiEvent : PasswordRecoveryCodeEvent() {
    data class OnNextClicked(val email: String, val code: String) : PasswordRecoveryCodeUiEvent()
    data class OnRepeatClicked(val email: String) : PasswordRecoveryCodeUiEvent()
}