package com.technostore.feature_login.password_recovery.presentation

import com.technostore.arch.mvi.Event

sealed class PasswordRecoveryEvent : Event {
    data object FirstPasswordIsEmpty : PasswordRecoveryEvent()
    data object FirstPasswordErrorSymbols : PasswordRecoveryEvent()
    data object FirstPasswordErrorMinLength : PasswordRecoveryEvent()
    data object FirstPasswordErrorMaxLength : PasswordRecoveryEvent()
    data object FirstPasswordIsValid : PasswordRecoveryEvent()

    data object SecondPasswordIsEmpty : PasswordRecoveryEvent()
    data object SecondPasswordErrorSymbols : PasswordRecoveryEvent()
    data object SecondPasswordErrorMinLength : PasswordRecoveryEvent()
    data object SecondPasswordErrorMaxLength : PasswordRecoveryEvent()
    data object SecondPasswordIsValid : PasswordRecoveryEvent()


    data object PasswordsAreBroken : PasswordRecoveryEvent()

    data object StartLoading : PasswordRecoveryEvent()
    data object EndLoading : PasswordRecoveryEvent()
}

sealed class PasswordRecoveryUIEvent : PasswordRecoveryEvent() {
    class OnNextClicked(
        val firstPassword: String,
        val secondPassword: String
    ) : PasswordRecoveryUIEvent()
}