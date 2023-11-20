package com.technostore.app_store.store

interface AppStore {
    var refreshToken: String?
    var accessToken: String?
    var email: String?
    var id: String?
    var isActive: Boolean
    var isOnboardingShown: Boolean

    fun refresh(
        refreshToken: String,
        accessToken: String,
        expireTimeRefreshToken: String,
        expireTimeAccessToken: String,
        id: String,
        email: String
    )
}