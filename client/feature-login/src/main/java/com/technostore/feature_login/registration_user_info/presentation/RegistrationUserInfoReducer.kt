package com.technostore.feature_login.registration_user_info.presentation

import com.technostore.arch.mvi.Reducer

class RegistrationUserInfoReducer : Reducer<RegistrationUserInfoState, RegistrationUserInfoEvent> {
    override fun reduce(
        currentState: RegistrationUserInfoState,
        event: RegistrationUserInfoEvent
    ): RegistrationUserInfoState = when (event) {
        is RegistrationUserInfoEvent.NameIsValid -> currentState.copy(
            firstNameValidation = NameValidation.SUCCESS
        )

        is RegistrationUserInfoEvent.NameIsEmpty -> currentState.copy(
            firstNameValidation = NameValidation.EMPTY,
            isLoading = false
        )

        is RegistrationUserInfoEvent.NameErrorLength -> currentState.copy(
            firstNameValidation = NameValidation.ERROR,
            isLoading = false
        )

        is RegistrationUserInfoEvent.LastNameIsValid -> currentState.copy(
            lastNameValidation = NameValidation.SUCCESS
        )

        is RegistrationUserInfoEvent.LastNameIsEmpty -> currentState.copy(
            lastNameValidation = NameValidation.EMPTY,
            isLoading = false
        )

        is RegistrationUserInfoEvent.LastNameErrorLength -> currentState.copy(
            lastNameValidation = NameValidation.ERROR,
            isLoading = false
        )

        is RegistrationUserInfoEvent.StartLoading -> currentState.copy(
            isLoading = true
        )

        is RegistrationUserInfoEvent.EndLoading -> currentState.copy(
            isLoading = false
        )

        else -> currentState
    }

}