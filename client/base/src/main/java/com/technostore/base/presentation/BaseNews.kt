package com.technostore.base.presentation

import com.technostore.arch.mvi.News

sealed class BaseNews : News() {
    data object OpenOnboarding : BaseNews()
    data object OpenLogin : BaseNews()
}