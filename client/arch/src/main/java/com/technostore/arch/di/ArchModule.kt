package com.technostore.arch.di

import com.technostore.arch.mvi.InitialState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ArchModule {
    @Provides
    fun provideInitialState(): InitialState {
        return InitialState()
    }
}
