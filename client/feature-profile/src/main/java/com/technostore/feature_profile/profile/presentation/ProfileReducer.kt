package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.mvi.Reducer

class ProfileReducer : Reducer<ProfileState, ProfileEvent> {
    override fun reduce(currentState: ProfileState, event: ProfileEvent): ProfileState {
        return when (event) {
            is ProfileEvent.StartLoading -> {
                currentState.copy(
                    isLoading = true
                )
            }

            is ProfileEvent.EndLoading -> {
                currentState.copy(
                    isLoading = false
                )
            }

            is ProfileEvent.ProfileLoaded -> {
                currentState.copy(
                    name = event.name,
                    lastName = event.lastName,
                    email = event.image,
                    image = event.image
                )
            }

            else -> currentState
        }
    }
}