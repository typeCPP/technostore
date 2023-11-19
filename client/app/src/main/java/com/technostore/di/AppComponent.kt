package com.technostore.di

import com.technostore.activity.MainActivity
import com.technostore.app_store.di.AppStoreModule
import com.technostore.arch.di.ArchModule
import com.technostore.feature_login.di.LoginModule
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
        LoginModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
