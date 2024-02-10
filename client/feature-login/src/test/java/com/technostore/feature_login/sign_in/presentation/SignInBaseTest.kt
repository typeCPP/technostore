package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.Store
import io.mockk.mockk

open class SignInBaseTest {
    protected val email = " test@yandex.ru "
    protected val trimEmail = email.trim()
    protected val password = "testPassword123"
    protected val defaultState = SignInState()
    protected val store = mockk<Store<SignInState, SignInEvent>>(relaxed = true)
}