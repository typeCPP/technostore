package com.technostore.feature_main_page.di

import com.technostore.feature_main_page.business.MainRepository
import com.technostore.feature_main_page.business.MainRepositoryImpl
import com.technostore.feature_main_page.main_page.presentation.MainEffectHandler
import com.technostore.feature_main_page.main_page.presentation.MainReducer
import com.technostore.network.service.ProductService
import com.technostore.shared_search.business.SharedSearchRepository
import com.technostore.shared_search.business.model.mapper.ProductSearchMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideMainRepository(
        productService: ProductService,
        productSearchMapper: ProductSearchMapper
    ): MainRepository {
        return MainRepositoryImpl(productService, productSearchMapper)
    }

    /* Main */
    @Provides
    fun provideSearchReducer(): MainReducer {
        return MainReducer()
    }

    @Provides
    fun provideSearchEffectHandler(
        sharedSearchRepository: SharedSearchRepository,
        mainRepository: MainRepository
    ): MainEffectHandler {
        return MainEffectHandler(sharedSearchRepository, mainRepository)
    }
}