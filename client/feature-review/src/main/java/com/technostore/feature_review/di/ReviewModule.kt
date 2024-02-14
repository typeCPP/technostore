package com.technostore.feature_review.di

import com.technostore.arch.mvi.Store
import com.technostore.feature_review.business.ReviewRepository
import com.technostore.feature_review.business.ReviewRepositoryImpl
import com.technostore.feature_review.business.model.mapper.ReviewMapper
import com.technostore.network.service.ReviewService
import com.technostore.feature_review.review.presentation.ReviewEffectHandler
import com.technostore.feature_review.review.presentation.ReviewEvent
import com.technostore.feature_review.review.presentation.ReviewReducer
import com.technostore.feature_review.review.presentation.ReviewState
import com.technostore.feature_review.review_list.presentation.ReviewListEffectHandler
import com.technostore.feature_review.review_list.presentation.ReviewListEvent
import com.technostore.feature_review.review_list.presentation.ReviewListReducer
import com.technostore.feature_review.review_list.presentation.ReviewListState
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

    @Provides
    fun provideReviewListState(): ReviewListState {
        return ReviewListState()
    }

    @Provides
    fun provideReviewListStore(
        initialState: ReviewListState,
        reducer: ReviewListReducer,
        effectHandler: ReviewListEffectHandler
    ): Store<ReviewListState, ReviewListEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
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

    @Provides
    fun provideReviewState(): ReviewState {
        return ReviewState()
    }

    @Provides
    fun provideReviewStore(
        initialState: ReviewState,
        reducer: ReviewReducer,
        effectHandler: ReviewEffectHandler
    ): Store<ReviewState, ReviewEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }
}