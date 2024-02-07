package com.technostore.feature_login.registration.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.common_ui.EMAIL_REGEX
import com.technostore.feature_login.common_ui.PASSWORD_REGEX


class RegistrationEffectHandler(
    private val loginRepository: LoginRepository
) : EffectHandler<RegistrationState, RegistrationEvent> {
    override suspend fun process(
        event: RegistrationEvent,
        currentState: RegistrationState,
        store: Store<RegistrationState, RegistrationEvent>
    ) {
        when (event) {
            is RegistrationUiEvent.OnRegistrationClicked -> {
                val emailTrim = event.email.trim()
                if (emailTrim.isEmpty()) {
                    store.dispatch(RegistrationEvent.EmailIsEmpty)
                    store.dispatch(RegistrationEvent.FirstPasswordIsValid)
                    store.dispatch(RegistrationEvent.SecondPasswordIsValid)
                    return
                }
                if (emailTrim.length > 255) {
                    store.dispatch(RegistrationEvent.EmailMaxLength)
                    store.dispatch(RegistrationEvent.FirstPasswordIsValid)
                    store.dispatch(RegistrationEvent.SecondPasswordIsValid)
                    return
                }
                if(!EMAIL_REGEX.toRegex().matches(emailTrim)){
                    store.dispatch(RegistrationEvent.EmailIsInvalid)
                    store.dispatch(RegistrationEvent.FirstPasswordIsValid)
                    store.dispatch(RegistrationEvent.SecondPasswordIsValid)
                    return
                }
                store.dispatch(RegistrationEvent.EmailIsValid)

                if (event.firstPassword.isEmpty()) {
                    store.dispatch(RegistrationEvent.FirstPasswordIsEmpty)
                    return
                }
                if (event.firstPassword.length < 6) {
                    store.dispatch(RegistrationEvent.FirstPasswordErrorMinLength)
                    return
                }
                if (event.firstPassword.length > 255) {
                    store.dispatch(RegistrationEvent.FirstPasswordErrorMaxLength)
                    return
                }
                if (!PASSWORD_REGEX.toRegex().matches(event.firstPassword)) {
                    store.dispatch(RegistrationEvent.FirstPasswordErrorSymbols)
                    return
                }
                store.dispatch(RegistrationEvent.FirstPasswordIsValid)
                if (event.secondPassword.isEmpty()) {
                    store.dispatch(RegistrationEvent.SecondPasswordIsEmpty)
                    return
                }
                if (event.secondPassword.length < 6) {
                    store.dispatch(RegistrationEvent.SecondPasswordErrorMinLength)
                    return
                }
                if (event.secondPassword.length > 255) {
                    store.dispatch(RegistrationEvent.SecondPasswordErrorMaxLength)
                    return
                }
                if (!PASSWORD_REGEX.toRegex().matches(event.secondPassword)) {
                    store.dispatch(RegistrationEvent.SecondPasswordErrorSymbols)
                    return
                }
                store.dispatch(RegistrationEvent.SecondPasswordIsValid)

                if (event.firstPassword != event.secondPassword) {
                    store.dispatch(RegistrationEvent.PasswordsAreBroken)
                    return
                }

                store.dispatch(RegistrationEvent.StartLoading)
                val checkEmailExistsResult = loginRepository.checkEmailExists(emailTrim)
                store.dispatch(RegistrationEvent.EndLoading)
                if (checkEmailExistsResult is Result.Error) {
                    store.acceptNews(RegistrationNews.ShowErrorToast)
                    return
                }
                if (checkEmailExistsResult is Result.Success) {
                    val isEmailExists = checkEmailExistsResult.data!!
                    if (isEmailExists) {
                        store.dispatch(RegistrationEvent.EmailExists)
                        return
                    }
                }
                store.acceptNews(
                    RegistrationNews.OpenRegistrationDataPage(
                        email = emailTrim,
                        password = event.firstPassword
                    )
                )
            }

            is RegistrationUiEvent.OnLoginClicked -> store.acceptNews(RegistrationNews.OpenSignInPage)

            else -> Unit
        }
    }
}