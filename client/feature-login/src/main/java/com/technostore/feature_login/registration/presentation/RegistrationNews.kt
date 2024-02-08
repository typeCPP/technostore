package com.technostore.feature_login.registration.presentation

import com.technostore.arch.mvi.News

sealed class RegistrationNews : News() {
    data object ShowErrorToast : RegistrationNews()
    data object OpenSignInPage : RegistrationNews()
    data class OpenRegistrationDataPage(val email: String, val password: String) : RegistrationNews()
}