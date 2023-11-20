package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_login.common_ui.EmailValidation

data class PasswordRecoveryEmailState(
    val isLoading: Boolean = false,
    val emailValidation: EmailValidation = EmailValidation.SUCCESS
) : State