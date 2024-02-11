package com.technostore.feature_profile.change_password.presentation

import com.technostore.arch.mvi.Store
import io.mockk.mockk

open class ChangePasswordBaseTest {
    protected val store = mockk<Store<ChangePasswordState, ChangePasswordEvent>>(relaxed = true)
    protected val password = "password123"
}