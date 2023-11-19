package com.technostore.app_store.store

interface AppStore {
    var refreshToken: Long
    var accessToken: Long
    var email: String?
    var id: String?
    var isActive: Boolean

    fun refresh(
        refreshToken: String,
        accessToken: String,
        expireTimeRefreshToken: String,
        expireTimeAccessToken: String,
        id: String,
        email: String
    )
}