package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.arch.mvi.News

sealed class PasswordRecoveryEmailNews : News() {
    class OpenCodePage(val email: String) : PasswordRecoveryEmailNews()
    data object ShowErrorToast : PasswordRecoveryEmailNews()
}