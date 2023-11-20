package com.technostore.feature_login.confirm_code.presentation

import com.technostore.arch.mvi.Reducer

class ConfirmationCodeReducer : Reducer<ConfirmationCodeState, ConfirmationCodeEvent> {
    override fun reduce(
        currentState: ConfirmationCodeState,
        event: ConfirmationCodeEvent
    ): ConfirmationCodeState {
        return when (event) {
            is ConfirmationCodeEvent.StartLoading -> currentState.copy(
                isLoading = true
            )

            is ConfirmationCodeEvent.EndLoading -> currentState.copy(
                isLoading = false
            )

            else -> currentState
        }
    }
}