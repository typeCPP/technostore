package com.technostore.di

import com.technostore.business.ReviewRepository
import com.technostore.business.ReviewRepositoryImpl
import com.technostore.business.model.mapper.ReviewMapper
import com.technostore.network.service.ReviewService
import com.technostore.review.presentation.ReviewEffectHandler
import com.technostore.review.presentation.ReviewReducer
import com.technostore.review_list.presentation.ReviewListEffectHandler
import com.technostore.review_list.presentation.ReviewListReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ReviewModule {
    @Provides
    fun provideReviewMapper(): ReviewMapper {
        return ReviewMapper()
    }

    @Provides
    fun provideReviewRepository(
        reviewService: ReviewService,
        reviewMapper: ReviewMapper
    ): ReviewRepository {
        return ReviewRepositoryImpl(reviewService, reviewMapper)
    }

    /* Review list */
    @Provides
    fun provideReviewListReducer(): ReviewListReducer {
        return ReviewListReducer()
    }

    @Provides
    fun provideReviewListEffectHandler(reviewRepository: ReviewRepository): ReviewListEffectHandler {
        return ReviewListEffectHandler(reviewRepository)
    }

    /* Review */
    @Provides
    fun provideReviewReducer(): ReviewReducer {
        return ReviewReducer()
    }

    @Provides
    fun provideReviewEffectHandler(): ReviewEffectHandler {
        return ReviewEffectHandler()
    }
}