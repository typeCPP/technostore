package com.technostore.feature_login.sign_in.presentation

import com.technostore.feature_login.registration.presentation.RegistrationState

open class SignInBaseTest {
    protected val email = " test@yandex.ru "
    protected val trimEmail = email.trim()
    protected val password = "testPassword123"
    protected val defaultState = SignInState()
}