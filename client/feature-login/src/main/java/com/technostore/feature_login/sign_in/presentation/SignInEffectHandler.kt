package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.business.sign_in.error.SignInError

class SignInEffectHandler(private val loginRepository: LoginRepository) :
    EffectHandler<SignInState, SignInEvent> {
    override suspend fun process(
        event: SignInEvent,
        currentState: SignInState,
        store: Store<SignInState, SignInEvent>
    ) {
        when (event) {
            is SignInUiEvent.OnSignInClicked -> {
                if (event.email == null || event.email.trim().isEmpty()) {
                    store.dispatch(SignInEvent.EmailIsEmpty)
                    return
                } else {
                    store.dispatch(SignInEvent.EmailIsValid)
                }
                if (event.password == null || event.password.trim().isEmpty()) {
                    store.dispatch(SignInEvent.PasswordIsEmpty)
                    return
                } else {
                    store.dispatch(SignInEvent.PasswordIsValid)
                }
                store.dispatch(SignInEvent.StartLoading)
                val result = loginRepository.signIn(
                    email = event.email,
                    password = event.password
                )
                if (result is Result.Success) {
                    store.acceptNews(SignInNews.OpenMainPage)
                }
                if (result is Result.Error) {
                    when (result.error) {
                        SignInError.ErrorEmail -> store.dispatch(SignInEvent.EmailInvalid)
                        SignInError.ErrorPassword -> store.dispatch(SignInEvent.PasswordInvalid)
                        else -> {
                            store.dispatch(SignInEvent.EndLoading)
                            store.acceptNews(SignInNews.ShowErrorToast)
                        }
                    }
                }
            }

            is SignInUiEvent.OnRegistrationClicked -> store.acceptNews(SignInNews.OpenRegistrationPage)
            is SignInUiEvent.OnForgotPasswordClicked -> store.acceptNews(SignInNews.OpenPasswordRecoveryPage)
            else -> {}
        }
    }

}