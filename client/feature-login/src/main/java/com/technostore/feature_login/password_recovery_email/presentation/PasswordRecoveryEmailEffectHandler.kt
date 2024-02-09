package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.common_ui.EMAIL_REGEX

class PasswordRecoveryEmailEffectHandler(private val loginRepository: LoginRepository) :
    EffectHandler<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent> {
    override suspend fun process(
        event: PasswordRecoveryEmailEvent,
        currentState: PasswordRecoveryEmailState,
        store: Store<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent>
    ) {
        when (event) {
            is PasswordRecoveryEmailUIEvent.OnNextClicked -> {
                val emailTrim = event.email.trim()
                if (emailTrim.isEmpty()) {
                    store.dispatch(PasswordRecoveryEmailEvent.EmailIsEmpty)
                    return
                }
                if (emailTrim.length > 255) {
                    store.dispatch(PasswordRecoveryEmailEvent.EmailMaxLength)
                    return
                }
                if (!EMAIL_REGEX.toRegex().matches(emailTrim)) {
                    store.dispatch(PasswordRecoveryEmailEvent.EmailIsInvalid)
                    return
                }
                store.dispatch(PasswordRecoveryEmailEvent.EmailIsValid)
                store.dispatch(PasswordRecoveryEmailEvent.StartLoading)
                val checkEmailExistsResult = loginRepository.checkEmailExists(emailTrim)
                if (checkEmailExistsResult is Result.Error) {
                    store.dispatch(PasswordRecoveryEmailEvent.EndLoading)
                    store.acceptNews(PasswordRecoveryEmailNews.ShowErrorToast)
                    return
                }
                if (checkEmailExistsResult is Result.Success) {
                    val isEmailExists = checkEmailExistsResult.data
                    if (isEmailExists == null || !isEmailExists) {
                        store.dispatch(PasswordRecoveryEmailEvent.EndLoading)
                        store.dispatch(PasswordRecoveryEmailEvent.EmailNotExists)
                        return
                    }
                }

                val sendCodeResult = loginRepository.sendCodeForRecoveryPassword(email = emailTrim)
                store.dispatch(PasswordRecoveryEmailEvent.EndLoading)
                if (sendCodeResult is Result.Error) {
                    store.acceptNews(PasswordRecoveryEmailNews.ShowErrorToast)
                    return
                }
                store.acceptNews(PasswordRecoveryEmailNews.OpenCodePage(emailTrim))
            }

            else -> {}
        }
    }

}