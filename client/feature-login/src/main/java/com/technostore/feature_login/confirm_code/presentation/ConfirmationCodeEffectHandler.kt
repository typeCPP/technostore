package com.technostore.feature_login.confirm_code.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository

class ConfirmationCodeEffectHandler(
    private val loginRepository: LoginRepository
) : EffectHandler<ConfirmationCodeState, ConfirmationCodeEvent> {
    override suspend fun process(
        event: ConfirmationCodeEvent,
        currentState: ConfirmationCodeState,
        store: Store<ConfirmationCodeState, ConfirmationCodeEvent>
    ) {
        when (event) {
            is ConfirmationCodeUIEvent.OnConfirmCode -> {
                store.dispatch(ConfirmationCodeEvent.StartLoading)
                val result = loginRepository.checkRecoveryCodeForAccountConfirmations(
                    code = event.code,
                    email = event.email
                )
                store.dispatch(ConfirmationCodeEvent.EndLoading)
                when (result) {
                    is Result.Error -> store.acceptNews(ConfirmationCodeNews.ShowErrorToast)
                    is Result.Success -> {
                        if (result.data != null && result.data == true) {
                            store.acceptNews(ConfirmationCodeNews.OpenMainPage)
                        } else {
                            store.acceptNews(ConfirmationCodeNews.CodeIsInvalid)
                        }
                    }
                }
            }

            else -> {}
        }
    }

}