package com.technostore.di

import com.technostore.app_store.di.AppStoreModule
import com.technostore.arch.di.ArchModule
import com.technostore.base.di.BaseModule
import com.technostore.feature_login.di.LoginModule
import com.technostore.feature_profile.di.ProfileModule
import com.technostore.network.di.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppStoreModule::class,
        ArchModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        BaseModule::class,
        LoginModule::class,
        ProfileModule::class
    ]
)
interface AppComponent