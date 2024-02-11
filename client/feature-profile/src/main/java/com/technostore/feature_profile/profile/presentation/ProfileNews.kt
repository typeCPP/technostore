package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.mvi.News

sealed class ProfileNews : News() {
    data object OpenOrderHistoryPage : ProfileNews()
    data object ShowErrorToast : ProfileNews()
    data object Logout : ProfileNews()
    data object OpenChangePasswordPage : ProfileNews()
    data class OpenChangeProfilePage(
        val name: String,
        val lastName: String,
        val photoUrl: String
    ) : ProfileNews()
}