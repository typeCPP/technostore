package com.technostore.feature_review.review_list.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_review.business.ReviewRepository

class ReviewListEffectHandler(
    private val reviewRepository: ReviewRepository
) : EffectHandler<ReviewListState, ReviewListEvent> {
    override suspend fun process(
        event: ReviewListEvent,
        currentState: ReviewListState,
        store: Store<ReviewListState, ReviewListEvent>
    ) {
        when (event) {
            is ReviewListUiEvent.Init -> {
                store.dispatch(ReviewListEvent.StartLoading)
                val result = reviewRepository.getReviews(event.productId)
                when (result) {
                    is Result.Success -> {
                        val data=result.data
                        if (data!= null) {
                            store.dispatch(ReviewListEvent.EndLoading)
                            store.dispatch(ReviewListEvent.OnDataLoaded(data))
                            store.acceptNews(ReviewListNews.ShowReviews(data))
                        } else {
                            store.acceptNews(ReviewListNews.ShowErrorToast)
                        }
                    }

                    is Result.Error -> {
                        store.acceptNews(ReviewListNews.ShowErrorToast)
                    }
                }
            }

            ReviewListUiEvent.OnBackClicked -> {
                store.acceptNews(ReviewListNews.OpenPreviousPage)
            }

            is ReviewListUiEvent.OnReviewClicked -> {
                store.acceptNews(ReviewListNews.OpenReviewPage(event.review))
            }

            ReviewListUiEvent.OnAllReviewsClicked -> {
                store.acceptNews(ReviewListNews.ShowReviews(currentState.allReviews))
            }

            ReviewListUiEvent.OnNegativeReviewsClicked -> {
                store.acceptNews(ReviewListNews.ShowReviews(currentState.negativeReviews))
            }

            ReviewListUiEvent.OnNeutralReviewsClicked -> {
                store.acceptNews(ReviewListNews.ShowReviews(currentState.neutralReviews))
            }

            ReviewListUiEvent.OnPositiveReviewsClicked -> {
                store.acceptNews(ReviewListNews.ShowReviews(currentState.positiveReviews))
            }

            else -> {}
        }
    }
}