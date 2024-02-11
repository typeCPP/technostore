package com.technostore.feature_login.registration.presentation

import com.technostore.arch.mvi.Store
import io.mockk.mockk

open class RegistrationBaseTest {
    protected val email = " test@yandex.ru "
    protected val trimEmail = email.trim()
    protected val password = "testPassword123"
    protected val defaultState = RegistrationState()
    protected val store = mockk<Store<RegistrationState, RegistrationEvent>>(relaxed = true)
}