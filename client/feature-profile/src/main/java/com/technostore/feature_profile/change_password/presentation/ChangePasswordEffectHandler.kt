package com.technostore.feature_profile.change_password.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.ProfileRepository

class ChangePasswordEffectHandler(
    private val profileRepository: ProfileRepository
) : EffectHandler<ChangePasswordState, ChangePasswordEvent> {
    override suspend fun process(
        event: ChangePasswordEvent,
        currentState: ChangePasswordState,
        store: Store<ChangePasswordState, ChangePasswordEvent>
    ) {
        when (event) {
            is ChangePasswordUiEvent.OnChangePasswordClicked -> {
                if (event.oldPassword.isEmpty()) {
                    store.acceptNews(ChangePasswordNews.OldPasswordIsEmpty)
                    return
                }
                if (event.newPassword.isEmpty()) {
                    store.acceptNews(ChangePasswordNews.NewPasswordIsEmpty)
                    return
                }
                if (event.newRepeatPassword.isEmpty()) {
                    store.acceptNews(ChangePasswordNews.NewRepeatPasswordIsEmpty)
                    return
                }
                if (event.newPassword != event.newRepeatPassword) {
                    store.acceptNews(ChangePasswordNews.PasswordsIsNotEquals)
                    return
                }
                store.dispatch(ChangePasswordEvent.StartLoading)
                val response = profileRepository.changePassword(
                    oldPassword = event.oldPassword,
                    newPassword = event.newPassword
                )
                store.dispatch(ChangePasswordEvent.EndLoading)
                when (response) {
                    is Result.Success -> {
                        store.acceptNews(ChangePasswordNews.OpenPreviousPage)
                    }

                    is Result.Error -> {
                        if (response.error != null) {
                            store.acceptNews(ChangePasswordNews.WrongOldPassword)
                        } else {
                            store.acceptNews(ChangePasswordNews.ShowErrorToast)
                        }
                    }
                }
            }

            is ChangePasswordUiEvent.OnBackButtonClicked -> {
                store.acceptNews(ChangePasswordNews.OpenPreviousPage)
            }

            else -> {}
        }
    }
}