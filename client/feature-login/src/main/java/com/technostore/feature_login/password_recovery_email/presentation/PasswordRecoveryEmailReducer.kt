package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.arch.mvi.Reducer
import com.technostore.feature_login.common_ui.EmailValidation

class PasswordRecoveryEmailReducer :
    Reducer<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent> {
    override fun reduce(
        currentState: PasswordRecoveryEmailState,
        event: PasswordRecoveryEmailEvent
    ): PasswordRecoveryEmailState {
        return when (event) {
            is PasswordRecoveryEmailEvent.EmailIsEmpty -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.EMPTY
            )

            PasswordRecoveryEmailEvent.EmailMaxLength -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.ERROR_MAX_LENGTH
            )

            PasswordRecoveryEmailEvent.EmailIsInvalid -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.ERROR
            )

            PasswordRecoveryEmailEvent.EmailNotExists -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.NOT_EXISTS
            )

            PasswordRecoveryEmailEvent.EmailIsValid -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.SUCCESS
            )

            PasswordRecoveryEmailEvent.StartLoading -> currentState.copy(
                isLoading = true
            )

            PasswordRecoveryEmailEvent.EndLoading -> currentState.copy(
                isLoading = false
            )

            else -> currentState
        }
    }
}