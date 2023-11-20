package com.technostore.feature_login.password_recovery.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.common_ui.PASSWORD_REGEX

class PasswordRecoveryEffectHandler(private val loginRepository: LoginRepository) :
    EffectHandler<PasswordRecoveryState, PasswordRecoveryEvent> {
    override suspend fun process(
        event: PasswordRecoveryEvent,
        currentState: PasswordRecoveryState,
        store: Store<PasswordRecoveryState, PasswordRecoveryEvent>
    ) {
        when (event) {
            is PasswordRecoveryUIEvent.OnNextClicked -> {
                if (event.firstPassword.isEmpty()) {
                    store.dispatch(PasswordRecoveryEvent.FirstPasswordIsEmpty)
                    return
                }
                if (event.firstPassword.length < 6) {
                    store.dispatch(PasswordRecoveryEvent.FirstPasswordErrorMinLength)
                    return
                }
                if (event.firstPassword.length > 255) {
                    store.dispatch(PasswordRecoveryEvent.FirstPasswordErrorMaxLength)
                    return
                }
                if (!PASSWORD_REGEX.toRegex().matches(event.firstPassword)) {
                    store.dispatch(PasswordRecoveryEvent.FirstPasswordErrorSymbols)
                    return
                }
                store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid)
                if (event.secondPassword.isEmpty()) {
                    store.dispatch(PasswordRecoveryEvent.SecondPasswordIsEmpty)
                    return
                }
                if (event.secondPassword.length < 6) {
                    store.dispatch(PasswordRecoveryEvent.SecondPasswordErrorMinLength)
                    return
                }
                if (event.secondPassword.length > 255) {
                    store.dispatch(PasswordRecoveryEvent.SecondPasswordErrorMaxLength)
                    return
                }
                if (!PASSWORD_REGEX.toRegex().matches(event.secondPassword)) {
                    store.dispatch(PasswordRecoveryEvent.SecondPasswordErrorSymbols)
                    return
                }
                store.dispatch(PasswordRecoveryEvent.SecondPasswordIsValid)

                if (event.firstPassword != event.secondPassword) {
                    store.dispatch(PasswordRecoveryEvent.PasswordsAreBroken)
                    return
                }

                store.dispatch(PasswordRecoveryEvent.StartLoading)
                val result = loginRepository.changePassword(event.firstPassword)
                store.dispatch(PasswordRecoveryEvent.EndLoading)
                when (result) {
                    is Result.Success -> store.acceptNews(PasswordRecoveryNews.OpenLoginPage)
                    is Result.Error -> store.acceptNews(PasswordRecoveryNews.ShowErrorToast)
                }
            }

            else -> {}
        }
    }

}