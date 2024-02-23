package com.technostore.test.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.test.platform.app.InstrumentationRegistry
import com.technostore.app_store.store.AppStoreImpl

object TestExt {
    fun setupClass(
        isActive: Boolean = true,
        isOnBoardingShown: Boolean = true
    ) {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val appContext = instrumentation.targetContext.applicationContext as ContextWrapper

        val sharedPrefs =
            appContext.getSharedPreferences(AppStoreImpl.USER_DATA, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(AppStoreImpl.IS_ACTIVE, isActive).commit()
        sharedPrefs.edit().putBoolean(AppStoreImpl.IS_ONBOARDING_SHOWN, isOnBoardingShown).commit()
    }
}