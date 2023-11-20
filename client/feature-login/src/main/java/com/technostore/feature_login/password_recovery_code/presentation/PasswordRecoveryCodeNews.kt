package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.arch.mvi.News

sealed class PasswordRecoveryCodeNews : News() {
    data object ShowErrorToast : PasswordRecoveryCodeNews()
    data object OpenPasswordRecoveryPage : PasswordRecoveryCodeNews()
    data object CodeIsInvalid : PasswordRecoveryCodeNews()
    data object CodeErrorLength : PasswordRecoveryCodeNews()
}