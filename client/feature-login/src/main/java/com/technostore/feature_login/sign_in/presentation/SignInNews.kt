package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.News

sealed class SignInNews : News() {
    data object ShowErrorToast : SignInNews()
    data object OpenMainPage : SignInNews()
    data object OpenPasswordRecoveryPage : SignInNews()
    data object OpenRegistrationPage : SignInNews()
}