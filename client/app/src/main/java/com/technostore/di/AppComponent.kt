package com.technostore.di

import com.technostore.activity.MainActivity
import com.technostore.arch.di.ArchModule
import com.technostore.feature_login.welcome_page.di.WelcomePageModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ArchModule::class,
        ViewModelModule::class,
        WelcomePageModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
