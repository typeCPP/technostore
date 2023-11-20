package com.technostore.app_store.store

import android.content.SharedPreferences
import com.technostore.app_store.utils.booleanPref
import com.technostore.app_store.utils.longPref
import com.technostore.app_store.utils.stringNullablePref
import javax.inject.Inject

class AppStoreImpl @Inject constructor(sharedPreferences: SharedPreferences) :
    AppStore {
    companion object {
        const val USER_DATA = "user_data"
        const val REFRESH_TOKEN = "refreshToken"
        const val ACCESS_TOKEN = "accessToken"
        const val EXPIRE_TIME_REFRESH_TOKEN = "expireTimeRefreshToken"
        const val EXPIRE_TIME_ACCESS_TOKEN = "expireTimeAccessToken"
        const val ID = "id"
        const val EMAIL = "email"
        const val IS_ACTIVE = "isActive"
        const val IS_ONBOARDING_SHOWN = "isOnboardingShown"
    }

    override var refreshToken by sharedPreferences.stringNullablePref(REFRESH_TOKEN)
    override var accessToken by sharedPreferences.stringNullablePref(ACCESS_TOKEN)
    override var email by sharedPreferences.stringNullablePref(EMAIL)
    override var id by sharedPreferences.stringNullablePref(ID)
    override var isActive by sharedPreferences.booleanPref(IS_ACTIVE)
    override var isOnboardingShown by sharedPreferences.booleanPref(IS_ONBOARDING_SHOWN)
    private var expireTimeRefreshToken by sharedPreferences.longPref(EXPIRE_TIME_REFRESH_TOKEN)
    private var expireTimeAccessToken by sharedPreferences.longPref(EXPIRE_TIME_ACCESS_TOKEN)

    override fun refresh(
        refreshToken: String,
        accessToken: String,
        expireTimeRefreshToken: String,
        expireTimeAccessToken: String,
        id: String,
        email: String
    ) {
        this.refreshToken = refreshToken
        this.accessToken = accessToken
        this.expireTimeRefreshToken = expireTimeRefreshToken.toLong()
        this.expireTimeAccessToken = expireTimeAccessToken.toLong()
        this.id = id
        this.email = email
    }
}