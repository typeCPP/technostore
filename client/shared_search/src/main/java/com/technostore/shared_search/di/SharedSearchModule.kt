package com.technostore.shared_search.di

import com.technostore.network.service.ProductService
import com.technostore.shared_search.business.SharedSearchRepository
import com.technostore.shared_search.business.SharedSearchRepositoryImpl
import com.technostore.shared_search.filter.presentation.FilterEffectHandler
import com.technostore.shared_search.filter.presentation.FilterReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedSearchModule {
    @Provides
    @Singleton
    fun provideSharedSearchRepository(
        productService: ProductService
    ): SharedSearchRepository {
        return SharedSearchRepositoryImpl(productService)
    }

    @Provides
    fun provideFilterEffectHandler(sharedSearchRepository: SharedSearchRepository): FilterEffectHandler {
        return FilterEffectHandler(sharedSearchRepository)
    }

    @Provides
    fun provideFilterPageReducer(
    ): FilterReducer {
        return FilterReducer()
    }
}