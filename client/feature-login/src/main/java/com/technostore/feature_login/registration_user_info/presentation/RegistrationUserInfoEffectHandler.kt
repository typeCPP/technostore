package com.technostore.feature_login.registration_user_info.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository

class RegistrationUserInfoEffectHandler(
    private val loginRepository: LoginRepository
) : EffectHandler<RegistrationUserInfoState, RegistrationUserInfoEvent> {
    override suspend fun process(
        event: RegistrationUserInfoEvent,
        currentState: RegistrationUserInfoState,
        store: Store<RegistrationUserInfoState, RegistrationUserInfoEvent>
    ) {
        when (event) {
            is RegistrationUserInfoUiEvent.OnImageChanged -> store.acceptNews(
                RegistrationUserInfoNews.ChangeImage(event.uri)
            )

            is RegistrationUserInfoUiEvent.OnRegistrationClicked -> {
                val nameTrim = event.name.trim()
                if (event.name.isEmpty()) {
                    store.dispatch(RegistrationUserInfoEvent.NameIsEmpty)
                    return
                }
                if (nameTrim.length > 255) {
                    store.dispatch(RegistrationUserInfoEvent.NameErrorLength)
                    return
                }
                val lastNameTrim = event.lastName.trim()
                store.dispatch(RegistrationUserInfoEvent.NameIsValid)
                if (event.lastName.isEmpty()) {
                    store.dispatch(RegistrationUserInfoEvent.LastNameIsEmpty)
                    return
                }
                if (lastNameTrim.length > 255) {
                    store.dispatch(RegistrationUserInfoEvent.LastNameErrorLength)
                    return
                }
                store.dispatch(RegistrationUserInfoEvent.LastNameIsValid)
                store.dispatch(RegistrationUserInfoEvent.StartLoading)
                val result = loginRepository.signUp(
                    name = nameTrim,
                    lastName = lastNameTrim,
                    email = event.email,
                    password = event.password,
                    byteArray = event.byteArray
                )
                store.dispatch(RegistrationUserInfoEvent.EndLoading)
                if (result is Result.Error) {
                    store.acceptNews(RegistrationUserInfoNews.ShowErrorToast)
                    return
                }
                if (result is Result.Success) {
                    store.acceptNews(RegistrationUserInfoNews.OpenCodePage)
                }
            }

            else -> {}
        }
    }

}