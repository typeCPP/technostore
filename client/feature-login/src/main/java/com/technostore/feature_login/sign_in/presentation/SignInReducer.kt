package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.Reducer
import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.common_ui.PasswordValidation

class SignInReducer : Reducer<SignInState, SignInEvent> {
    override fun reduce(currentState: SignInState, event: SignInEvent): SignInState {
        return when (event) {
            is SignInEvent.EmailIsEmpty -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.EMPTY
            )

            is SignInEvent.EmailMaxLength -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.ERROR_MAX_LENGTH
            )

            is SignInEvent.EmailIsInvalid -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.ERROR
            )

            is SignInEvent.EmailNotExists -> currentState.copy(
                isLoading = false,
                emailValidation = EmailValidation.NOT_EXISTS
            )

            is SignInEvent.PasswordIsEmpty -> currentState.copy(
                isLoading = false,
                passwordValidation = PasswordValidation.EMPTY
            )

            is SignInEvent.PasswordInvalid -> currentState.copy(
                isLoading = false,
                passwordValidation = PasswordValidation.INCORRECT_PASSWORD
            )

            is SignInEvent.PasswordErrorMaxLength -> currentState.copy(
                isLoading = false,
                passwordValidation = PasswordValidation.ERROR_MAX_LENGTH
            )

            is SignInEvent.EmailIsValid -> currentState.copy(
                emailValidation = EmailValidation.SUCCESS
            )

            is SignInEvent.PasswordIsValid -> currentState.copy(
                passwordValidation = PasswordValidation.SUCCESS
            )

            is SignInEvent.StartLoading -> currentState.copy(
                isLoading = true
            )

            is SignInEvent.EndLoading -> currentState.copy(
                isLoading = false
            )

            else -> currentState
        }
    }
}