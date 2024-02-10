package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.arch.mvi.Store
import io.mockk.mockk

open class PasswordRecoveryCodeBaseTest {
    protected val store =
        mockk<Store<PasswordRecoveryCodeState, PasswordRecoveryCodeEvent>>(relaxed = true)
    protected val email = " email@yandex.ru"
    protected val code = " 123 "
}