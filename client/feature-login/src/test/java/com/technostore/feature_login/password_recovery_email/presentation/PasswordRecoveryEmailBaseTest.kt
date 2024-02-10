package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.arch.mvi.Store
import io.mockk.mockk

open class PasswordRecoveryEmailBaseTest {
    protected val email = " test@yandex.ru "
    protected val trimEmail = email.trim()
    protected val defaultState = PasswordRecoveryEmailState()
    protected val store =
        mockk<Store<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent>>(relaxed = true)
}