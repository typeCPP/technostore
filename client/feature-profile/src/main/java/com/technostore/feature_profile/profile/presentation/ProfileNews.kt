package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.mvi.News

sealed class ProfileNews : News() {
    data object OpenOrderHistoryPage : ProfileNews()
    data object ShowErrorToast : ProfileNews()
    data object Logout : ProfileNews()
    data object OpenChangePasswordPage : ProfileNews()
    data object OpenChangeProfilePage : ProfileNews()
}