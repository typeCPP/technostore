package com.technostore.feature_login.welcome_page.presentation

import com.technostore.arch.mvi.Event

sealed class WelcomePageEvent : Event {
    data object OnNextClicked : WelcomePageEvent()
}