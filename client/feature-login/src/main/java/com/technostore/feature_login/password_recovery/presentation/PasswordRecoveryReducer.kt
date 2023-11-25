package com.technostore.feature_login.password_recovery.presentation

import com.technostore.arch.mvi.Reducer
import com.technostore.feature_login.common_ui.PasswordValidation

class PasswordRecoveryReducer : Reducer<PasswordRecoveryState, PasswordRecoveryEvent> {
    override fun reduce(
        currentState: PasswordRecoveryState,
        event: PasswordRecoveryEvent
    ): PasswordRecoveryState {
        return when (event) {

            is PasswordRecoveryEvent.FirstPasswordIsEmpty -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.EMPTY
            )

            is PasswordRecoveryEvent.FirstPasswordErrorSymbols -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.ERROR_SYMBOL
            )

            is PasswordRecoveryEvent.FirstPasswordErrorMinLength -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.ERROR_MIN_LENGTH
            )

            is PasswordRecoveryEvent.FirstPasswordErrorMaxLength -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.ERROR_MAX_LENGTH
            )

            is PasswordRecoveryEvent.FirstPasswordIsValid -> currentState.copy(
                firstPasswordValidation = PasswordValidation.SUCCESS
            )

            is PasswordRecoveryEvent.SecondPasswordIsEmpty -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.EMPTY
            )

            is PasswordRecoveryEvent.SecondPasswordIsValid -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.SUCCESS
            )

            is PasswordRecoveryEvent.SecondPasswordErrorSymbols -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.ERROR_SYMBOL
            )

            is PasswordRecoveryEvent.SecondPasswordErrorMinLength -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.ERROR_MIN_LENGTH
            )

            is PasswordRecoveryEvent.SecondPasswordErrorMaxLength -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.ERROR_MAX_LENGTH
            )

            is PasswordRecoveryEvent.PasswordsAreBroken -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.DIFFERENT
            )

            is PasswordRecoveryEvent.StartLoading -> currentState.copy(
                isLoading = true
            )

            is PasswordRecoveryEvent.EndLoading -> currentState.copy(
                isLoading = false
            )

            else -> currentState
        }
    }
}