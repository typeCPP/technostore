package com.technostore.feature_login.welcome_page.presentation

import com.technostore.arch.mvi.News

sealed class WelcomePageNews : News() {
    data object OpenLoginPage : WelcomePageNews()
}