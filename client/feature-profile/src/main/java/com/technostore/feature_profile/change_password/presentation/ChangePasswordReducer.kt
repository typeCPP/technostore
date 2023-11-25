package com.technostore.feature_profile.change_password.presentation

import com.technostore.arch.mvi.Reducer

class ChangePasswordReducer : Reducer<ChangePasswordState, ChangePasswordEvent> {
    override fun reduce(
        currentState: ChangePasswordState,
        event: ChangePasswordEvent
    ): ChangePasswordState {
        return when (event) {
            is ChangePasswordEvent.StartLoading -> currentState.copy(isLoading = true)
            is ChangePasswordEvent.EndLoading -> currentState.copy(isLoading = false)
            else -> currentState
        }
    }
}