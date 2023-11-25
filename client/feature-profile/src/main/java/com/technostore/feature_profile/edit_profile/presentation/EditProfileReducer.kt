package com.technostore.feature_profile.edit_profile.presentation

import com.technostore.arch.mvi.Reducer

class EditProfileReducer : Reducer<EditProfileState, EditProfileEvent> {
    override fun reduce(currentState: EditProfileState, event: EditProfileEvent): EditProfileState {
        return when (event) {
            is EditProfileEvent.StartLoading -> currentState.copy(isLoading = true)
            is EditProfileEvent.EndLoading -> currentState.copy(isLoading = false)
            else -> currentState
        }
    }
}