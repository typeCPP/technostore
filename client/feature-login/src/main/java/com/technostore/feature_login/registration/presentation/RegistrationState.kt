package com.technostore.feature_login.registration.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.common_ui.PasswordValidation

data class RegistrationState(
    val isLoading: Boolean = false,
    val emailValidation: EmailValidation = EmailValidation.SUCCESS,
    val firstPasswordValidation: PasswordValidation = PasswordValidation.SUCCESS,
    val secondPasswordValidation: PasswordValidation = PasswordValidation.SUCCESS
) : State
