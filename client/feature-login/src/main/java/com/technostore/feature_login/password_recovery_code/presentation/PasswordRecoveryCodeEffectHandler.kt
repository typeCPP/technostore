package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository

class PasswordRecoveryCodeEffectHandler(private val loginRepository: LoginRepository) :
    EffectHandler<PasswordRecoveryCodeState, PasswordRecoveryCodeEvent> {
    override suspend fun process(
        event: PasswordRecoveryCodeEvent,
        currentState: PasswordRecoveryCodeState,
        store: Store<PasswordRecoveryCodeState, PasswordRecoveryCodeEvent>
    ) {
        when (event) {
            is PasswordRecoveryCodeUiEvent.OnRepeatClicked -> {
                val result = loginRepository.sendCodeForRecoveryPassword(
                    email = event.email
                )
                store.dispatch(PasswordRecoveryCodeEvent.EndLoading)
                if (result is Result.Error) {
                    store.acceptNews(PasswordRecoveryCodeNews.ShowErrorToast)
                }
            }

            is PasswordRecoveryCodeUiEvent.OnNextClicked -> {
                if (event.code.length > 255) {
                    store.acceptNews(PasswordRecoveryCodeNews.CodeErrorLength)
                    return
                }
                store.dispatch(PasswordRecoveryCodeEvent.StartLoading)
                val result = loginRepository.checkRecoveryCode(
                    code = event.code,
                    email = event.email
                )
                store.dispatch(PasswordRecoveryCodeEvent.EndLoading)
                when (result) {
                    is Result.Error -> store.acceptNews(PasswordRecoveryCodeNews.ShowErrorToast)
                    is Result.Success -> {
                        if (result.data != null && result.data == true) {
                            store.acceptNews(PasswordRecoveryCodeNews.OpenPasswordRecoveryPage)
                        } else {
                            store.acceptNews(PasswordRecoveryCodeNews.CodeIsInvalid)
                        }
                    }
                }
            }

            else -> {}
        }
    }

}