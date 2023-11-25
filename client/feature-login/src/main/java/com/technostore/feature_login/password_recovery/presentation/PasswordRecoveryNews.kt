package com.technostore.feature_login.password_recovery.presentation

import com.technostore.arch.mvi.News

sealed class PasswordRecoveryNews : News() {
    data object ShowErrorToast : PasswordRecoveryNews()
    data object OpenLoginPage : PasswordRecoveryNews()
}