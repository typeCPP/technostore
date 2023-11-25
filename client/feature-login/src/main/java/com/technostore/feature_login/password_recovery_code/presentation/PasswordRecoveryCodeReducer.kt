package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.arch.mvi.Reducer

class PasswordRecoveryCodeReducer : Reducer<PasswordRecoveryCodeState, PasswordRecoveryCodeEvent> {
    override fun reduce(
        currentState: PasswordRecoveryCodeState,
        event: PasswordRecoveryCodeEvent
    ): PasswordRecoveryCodeState {
        return when (event) {
            is PasswordRecoveryCodeEvent.StartLoading -> currentState.copy(
                isLoading = true
            )

            is PasswordRecoveryCodeEvent.EndLoading -> currentState.copy(
                isLoading = false
            )

            else -> currentState
        }
    }
}