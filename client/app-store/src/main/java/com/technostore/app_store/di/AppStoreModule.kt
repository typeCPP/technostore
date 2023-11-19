package com.technostore.app_store.di

import android.content.Context
import android.content.SharedPreferences
import com.technostore.app_store.store.AppStore
import com.technostore.app_store.store.AppStoreImpl
import com.technostore.app_store.store.AppStoreImpl.Companion.USER_DATA
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppStoreModule {
    @UserDataSharedPref
    @Provides
    fun provideUserDataSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideAppStore(@UserDataSharedPref sharedPreferences: SharedPreferences): AppStore {
        return AppStoreImpl(sharedPreferences)
    }
}