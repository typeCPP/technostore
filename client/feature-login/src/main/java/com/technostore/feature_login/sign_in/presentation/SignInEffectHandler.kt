package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.business.sign_in.error.SignInError
import com.technostore.feature_login.common_ui.EMAIL_REGEX

class SignInEffectHandler(
    private val loginRepository: LoginRepository
) : EffectHandler<SignInState, SignInEvent> {
    override suspend fun process(
        event: SignInEvent,
        currentState: SignInState,
        store: Store<SignInState, SignInEvent>
    ) {
        when (event) {
            is SignInUiEvent.OnSignInClicked -> {
                val emailTrim = event.email?.trim()
                if (emailTrim.isNullOrEmpty()) {
                    store.dispatch(SignInEvent.EmailIsEmpty)
                    store.dispatch(SignInEvent.PasswordIsValid)
                    return
                }
                if (emailTrim.length > 255) {
                    store.dispatch(SignInEvent.EmailMaxLength)
                    store.dispatch(SignInEvent.PasswordIsValid)
                    return
                }
                if (!EMAIL_REGEX.toRegex().matches(emailTrim)) {
                    store.dispatch(SignInEvent.EmailIsInvalid)
                    store.dispatch(SignInEvent.PasswordIsValid)
                    return
                }
                store.dispatch(SignInEvent.EmailIsValid)

                if (event.password.isNullOrEmpty()) {
                    store.dispatch(SignInEvent.PasswordIsEmpty)
                    return
                }
                if (event.password.length > 255) {
                    store.dispatch(SignInEvent.PasswordErrorMaxLength)
                    return
                }
                store.dispatch(SignInEvent.PasswordIsValid)

                store.dispatch(SignInEvent.StartLoading)
                val result = loginRepository.signIn(
                    email = emailTrim,
                    password = event.password
                )
                if (result is Result.Success) {
                    store.acceptNews(SignInNews.OpenMainPage)
                }
                if (result is Result.Error) {
                    when (result.error) {
                        SignInError.ErrorEmail -> store.dispatch(SignInEvent.EmailNotExists)
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