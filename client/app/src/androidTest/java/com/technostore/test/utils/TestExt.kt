package com.technostore.test.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.test.platform.app.InstrumentationRegistry
import com.technostore.app_store.store.AppStoreImpl

object TestExt {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val appContext = instrumentation.targetContext.applicationContext as ContextWrapper
    private val sharedPrefs =
        appContext.getSharedPreferences(AppStoreImpl.USER_DATA, Context.MODE_PRIVATE)

    fun setupClass(
        isActive: Boolean = true,
        isOnBoardingShown: Boolean = true
    ) {
        sharedPrefs.edit().putBoolean(AppStoreImpl.IS_ACTIVE, isActive).commit()
        sharedPrefs.edit().putBoolean(AppStoreImpl.IS_ONBOARDING_SHOWN, isOnBoardingShown).commit()
    }

    fun setupActiveUserClass(accessToken: String, refreshToken: String, email: String) {
        setupClass()

        sharedPrefs.edit().putString(AppStoreImpl.ACCESS_TOKEN, accessToken).commit()
        sharedPrefs.edit().putString(AppStoreImpl.REFRESH_TOKEN, refreshToken).commit()
        sharedPrefs.edit().putString(AppStoreImpl.EMAIL, email).commit()
        sharedPrefs.edit().putLong(AppStoreImpl.EXPIRE_TIME_ACCESS_TOKEN, 1741552428000).commit()
        sharedPrefs.edit().putLong(AppStoreImpl.EXPIRE_TIME_REFRESH_TOKEN, 1741552428000).commit()
    }
}