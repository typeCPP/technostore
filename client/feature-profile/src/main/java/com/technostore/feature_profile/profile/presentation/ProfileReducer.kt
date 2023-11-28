package com.technostore.feature_profile.profile.presentation

import com.technostore.app_store.store.AppStore
import com.technostore.arch.mvi.Reducer

class ProfileReducer(private val appStore: AppStore) : Reducer<ProfileState, ProfileEvent> {
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
                    email = appStore.email.orEmpty(),
                    image = event.image
                )
            }

            else -> currentState
        }
    }
}