package com.technostore.di

import com.technostore.app_store.di.AppStoreModule
import com.technostore.arch.di.ArchModule
import com.technostore.base.di.BaseModule
import com.technostore.feature_login.di.LoginModule
import com.technostore.feature_main_page.di.MainModule
import com.technostore.feature_order.di.OrderModule
import com.technostore.feature_profile.di.ProfileModule
import com.technostore.feature_search.di.SearchModule
import com.technostore.feature_shopping_cart.di.ShoppingCartModule
import com.technostore.network.di.NetworkModule
import com.technostore.shared_search.di.SharedSearchModule
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
        ProfileModule::class,
        SharedSearchModule::class,
        ProfileModule::class,
        ShoppingCartModule::class,
        ReviewModule::class,
        SearchModule::class,
        MainModule::class,
        OrderModule::class
    ]
)
interface AppComponent