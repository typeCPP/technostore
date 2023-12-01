package com.technostore.feature_product.di

import com.technostore.feature_product.business.ProductRepository
import com.technostore.feature_product.business.ProductRepositoryImpl
import com.technostore.feature_product.business.model.mapper.CategoryMapper
import com.technostore.feature_product.business.model.mapper.ProductDetailMapper
import com.technostore.feature_product.business.model.mapper.ReviewMapper
import com.technostore.feature_product.product.presentation.ProductEffectHandler
import com.technostore.feature_product.product.presentation.ProductReducer
import com.technostore.feature_product.product_description.presentation.ProductDescriptionEffectHandler
import com.technostore.feature_product.product_description.presentation.ProductDescriptionReducer
import com.technostore.network.service.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProductModule {

    @Provides
    fun provideCategoryMapper(): CategoryMapper {
        return CategoryMapper()
    }

    @Provides
    fun provideReviewMapper(): ReviewMapper {
        return ReviewMapper()
    }

    @Provides
    fun provideProductDetailMapper(
        categoryMapper: CategoryMapper,
        reviewMapper: ReviewMapper
    ): ProductDetailMapper {
        return ProductDetailMapper(categoryMapper, reviewMapper)
    }

    @Provides
    fun provideProductRepository(
        productService: ProductService,
        productDetailMapper: ProductDetailMapper
    ): ProductRepository {
        return ProductRepositoryImpl(
            productService,
            productDetailMapper
        )
    }

    /* Product */
    @Provides
    fun provideProductEffectHandler(productRepository: ProductRepository): ProductEffectHandler {
        return ProductEffectHandler(productRepository)
    }

    @Provides
    fun provideProductReducer(): ProductReducer {
        return ProductReducer()
    }

    /* Product description */
    @Provides
    fun provideProductDescriptionEffectHandler(): ProductDescriptionEffectHandler {
        return ProductDescriptionEffectHandler()
    }

    @Provides
    fun provideProductDescriptionReducer(): ProductDescriptionReducer {
        return ProductDescriptionReducer()
    }
}