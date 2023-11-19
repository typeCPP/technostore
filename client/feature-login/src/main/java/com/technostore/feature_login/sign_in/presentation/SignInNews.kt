package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.News

sealed class SignInNews : News() {
    object ShowErrorToast : SignInNews()
    object OpenMainPage : SignInNews()
    object OpenPasswordRecoveryPage : SignInNews()
    object OpenRegistrationPage : SignInNews()
}