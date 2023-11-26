package com.technostore.feature_profile.edit_profile.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.ProfileRepository

class EditProfileEffectHandler(
    private val profileRepository: ProfileRepository
) : EffectHandler<EditProfileState, EditProfileEvent> {
    override suspend fun process(
        event: EditProfileEvent,
        currentState: EditProfileState,
        store: Store<EditProfileState, EditProfileEvent>
    ) {
        when (event) {
            is EditProfileUiEvent.OnChangeProfileClicked -> {
                if (event.name.isEmpty()) {
                    store.acceptNews(EditProfileNews.NameIsEmpty)
                    return
                }
                if (event.lastName.isEmpty()) {
                    store.acceptNews(EditProfileNews.LastNameIsEmpty)
                    return
                }
                store.dispatch(EditProfileEvent.StartLoading)
                val result = profileRepository.editProfile(
                    name = event.name,
                    lastName = event.lastName,
                    byteArray = event.byteArray
                )
                store.dispatch(EditProfileEvent.EndLoading)
                when (result) {
                    is Result.Success -> {
                        store.acceptNews(EditProfileNews.OpenPreviousPage)
                    }

                    is Result.Error -> {
                        store.acceptNews(EditProfileNews.ShowErrorToast)
                    }
                }
            }

            is EditProfileUiEvent.OnBackButtonClicked -> {
                store.acceptNews(EditProfileNews.OpenPreviousPage)
            }

            is EditProfileUiEvent.OnImageChanged -> store.acceptNews(
                EditProfileNews.ChangeImage(event.uri)
            )

            else -> {}
        }
    }
}
