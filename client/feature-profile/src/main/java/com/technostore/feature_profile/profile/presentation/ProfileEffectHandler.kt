package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.ProfileRepository

class ProfileEffectHandler(private val profileRepository: ProfileRepository) :
    EffectHandler<ProfileState, ProfileEvent> {
    override suspend fun process(
        event: ProfileEvent,
        currentState: ProfileState,
        store: Store<ProfileState, ProfileEvent>
    ) {
        when (event) {
            is ProfileUiEvent.Init -> {
                store.dispatch(ProfileEvent.StartLoading)
                when (val response = profileRepository.getProfile()) {
                    is Result.Success ->
                        if (response.data != null) {
                            store.dispatch(ProfileEvent.EndLoading)
                            store.dispatch(
                                ProfileEvent.ProfileLoaded(
                                    name = response.data!!.firstName,
                                    lastName = response.data!!.lastName,
                                    email = response.data!!.email,
                                    image = response.data!!.image
                                )
                            )
                        }

                    is Result.Error -> {
                        store.acceptNews(ProfileNews.ShowErrorToast)
                    }
                }
            }

            ProfileUiEvent.OnLogoutClicked -> store.acceptNews(ProfileNews.Logout)
            ProfileUiEvent.OnChangeProfileClicked -> store.acceptNews(ProfileNews.OpenChangeProfilePage)
            ProfileUiEvent.OnChangePasswordClicked -> store.acceptNews(ProfileNews.OpenChangePasswordPage)
            ProfileUiEvent.OnOrderHistoryClicked -> store.acceptNews(ProfileNews.OpenOrderHistoryPage)
            else -> {}
        }
    }
}