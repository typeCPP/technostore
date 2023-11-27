package com.technostore.feature_product.di

import com.technostore.feature_product.business.ProductRepository
import com.technostore.feature_product.business.ProductRepositoryImpl
import com.technostore.feature_product.product.presentation.ProductEffectHandler
import com.technostore.feature_product.product.presentation.ProductReducer
import com.technostore.network.service.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProductModule {

    @Provides
    fun provideProductRepository(
        productService: ProductService
    ): ProductRepository {
        return ProductRepositoryImpl(
            productService
        )
    }

    /* Product */
    @Provides
    fun provideProductEffectHandler(productRepository: ProductRepository): ProductEffectHandler {
        return ProductEffectHandler(productRepository)
    }

    @Provides
    fun provideProductReducer(
    ): ProductReducer {
        return ProductReducer()
    }
}