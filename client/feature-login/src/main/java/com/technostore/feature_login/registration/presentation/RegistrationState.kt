package com.technostore.feature_login.registration.presentation

import com.technostore.arch.mvi.State

data class RegistrationState(
    val isLoading: Boolean = false,
    val emailValidation: EmailValidation = EmailValidation.SUCCESS,
    val firstPasswordValidation: PasswordValidation = PasswordValidation.SUCCESS,
    val secondPasswordValidation: PasswordValidation = PasswordValidation.SUCCESS
) : State

enum class EmailValidation {
    SUCCESS,
    ERROR,
    EMPTY,
    EXISTS
}

enum class PasswordValidation {
    SUCCESS,
    ERROR_SYMBOL,
    ERROR_MIN_LENGTH,
    ERROR_MAX_LENGTH,
    EMPTY,
    DIFFERENT
}