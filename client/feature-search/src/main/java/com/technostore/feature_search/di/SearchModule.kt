package com.technostore.feature_search.di

import com.technostore.arch.mvi.Store
import com.technostore.feature_search.search.presentation.SearchEffectHandler
import com.technostore.feature_search.search.presentation.SearchEvent
import com.technostore.feature_search.search.presentation.SearchReducer
import com.technostore.feature_search.search.presentation.SearchState
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

    @Provides
    fun provideSearchState(): SearchState {
        return SearchState()
    }

    @Provides
    fun provideSearchStore(
        initialState: SearchState,
        reducer: SearchReducer,
        effectHandler: SearchEffectHandler
    ): Store<SearchState, SearchEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }
}