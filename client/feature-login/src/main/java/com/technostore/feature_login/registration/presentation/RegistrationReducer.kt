package com.technostore.feature_login.registration.presentation

import com.technostore.arch.mvi.Reducer
import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.common_ui.PasswordValidation

class RegistrationReducer : Reducer<RegistrationState, RegistrationEvent> {
    override fun reduce(
        currentState: RegistrationState,
        event: RegistrationEvent
    ): RegistrationState {
        return when (event) {
            is RegistrationEvent.EmailIsEmpty -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.EMPTY
            )

            is RegistrationEvent.EmailMaxLength -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.ERROR_MAX_LENGTH
            )

            is RegistrationEvent.EmailIsInvalid -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.ERROR
            )

            is RegistrationEvent.FirstPasswordIsEmpty -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.EMPTY
            )

            is RegistrationEvent.FirstPasswordErrorSymbols -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.ERROR_SYMBOL
            )


            is RegistrationEvent.EmailExists -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.EXISTS
            )

            is RegistrationEvent.EmailIsValid -> currentState.copy(
                emailValidation = EmailValidation.SUCCESS
            )

            is RegistrationEvent.FirstPasswordErrorMinLength -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.ERROR_MIN_LENGTH
            )

            is RegistrationEvent.FirstPasswordErrorMaxLength -> currentState.copy(
                isLoading = false,
                firstPasswordValidation = PasswordValidation.ERROR_MAX_LENGTH
            )

            is RegistrationEvent.FirstPasswordIsValid -> currentState.copy(
                firstPasswordValidation = PasswordValidation.SUCCESS
            )

            is RegistrationEvent.SecondPasswordIsEmpty -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.EMPTY
            )

            is RegistrationEvent.SecondPasswordIsValid -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.SUCCESS
            )

            is RegistrationEvent.SecondPasswordErrorSymbols -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.ERROR_SYMBOL
            )

            is RegistrationEvent.SecondPasswordErrorMinLength -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.ERROR_MIN_LENGTH
            )

            is RegistrationEvent.SecondPasswordErrorMaxLength -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.ERROR_MAX_LENGTH
            )

            is RegistrationEvent.PasswordsAreBroken -> currentState.copy(
                isLoading = false,
                secondPasswordValidation = PasswordValidation.DIFFERENT
            )

            is RegistrationEvent.StartLoading -> currentState.copy(
                isLoading = true
            )

            is RegistrationEvent.EndLoading -> currentState.copy(
                isLoading = false
            )

            else -> currentState
        }
    }
}