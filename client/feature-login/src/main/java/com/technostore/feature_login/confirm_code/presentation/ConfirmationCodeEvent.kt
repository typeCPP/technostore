package com.technostore.feature_login.confirm_code.presentation

import com.technostore.arch.mvi.Event

sealed class ConfirmationCodeEvent : Event {
    data object StartLoading : ConfirmationCodeEvent()
    data object EndLoading: ConfirmationCodeEvent()
}

sealed class ConfirmationCodeUIEvent : ConfirmationCodeEvent() {
    class OnConfirmCode(val email: String, val code: String) : ConfirmationCodeUIEvent()
}