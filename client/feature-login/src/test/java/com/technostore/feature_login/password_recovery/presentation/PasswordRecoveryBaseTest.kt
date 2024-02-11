package com.technostore.feature_login.password_recovery.presentation

import com.technostore.arch.mvi.Store
import io.mockk.mockk

open class PasswordRecoveryBaseTest {
    protected val store = mockk<Store<PasswordRecoveryState, PasswordRecoveryEvent>>(relaxed = true)
    protected val firstPassword = "Password123"
    protected val secondPassword = "Password1"
}