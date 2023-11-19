package com.technostore.base.di

import com.technostore.app_store.store.AppStore
import com.technostore.base.presentation.BaseEffectHandler
import com.technostore.base.presentation.BaseReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BaseModule {
    @Provides
    fun provideBaseEffectHandler(appStore: AppStore): BaseEffectHandler {
        return BaseEffectHandler(appStore)
    }

    @Provides
    fun provideStateReducer(): BaseReducer {
        return BaseReducer()
    }
}