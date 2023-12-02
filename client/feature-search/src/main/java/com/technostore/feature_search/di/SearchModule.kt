package com.technostore.feature_search.di

import com.technostore.feature_search.search.presentation.SearchEffectHandler
import com.technostore.feature_search.search.presentation.SearchReducer
import com.technostore.shared_search.business.SharedSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SearchModule {

    /* Search */
    @Provides
    fun provideSearchReducer(): SearchReducer {
        return SearchReducer()
    }

    @Provides
    fun provideSearchEffectHandler(sharedSearchRepository: SharedSearchRepository): SearchEffectHandler {
        return SearchEffectHandler(sharedSearchRepository)
    }
}