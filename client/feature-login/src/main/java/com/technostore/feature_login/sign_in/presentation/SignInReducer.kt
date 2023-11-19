package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.Reducer

class SignInReducer : Reducer<SignInState, SignInEvent> {
    override fun reduce(currentState: SignInState, event: SignInEvent): SignInState {
        return when (event) {
            is SignInEvent.EmailIsEmpty -> currentState.copy(
                isLoading = false,
                emailValidation = Validation.EMPTY
            )

            is SignInEvent.PasswordIsEmpty -> currentState.copy(
                isLoading = false,
                passwordValidation = Validation.EMPTY
            )

            is SignInEvent.EmailInvalid -> currentState.copy(
                isLoading = false,
                emailValidation = Validation.ERROR
            )

            is SignInEvent.PasswordInvalid -> currentState.copy(
                isLoading = false,
                passwordValidation = Validation.ERROR
            )

            is SignInEvent.EmailIsValid -> currentState.copy(
                emailValidation = Validation.SUCCESS
            )

            is SignInEvent.PasswordIsValid -> currentState.copy(
                passwordValidation = Validation.SUCCESS
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