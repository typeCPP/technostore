package com.technostore.feature_review.business

import com.technostore.arch.result.Result
import com.technostore.feature_review.business.model.mapper.ReviewMapper
import com.technostore.feature_review.business.model.ReviewModel
import com.technostore.network.service.ReviewService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReviewRepositoryImpl(
    private val reviewService: ReviewService,
    private val reviewMapper: ReviewMapper
) : ReviewRepository {
    override suspend fun getReviews(productId: Long): Result<List<ReviewModel>> =
        withContext(Dispatchers.IO) {
            val response = reviewService.getReviewsByProductId(productId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return@withContext Result.Success(body.map {
                        reviewMapper.mapFromResponseToModel(
                            it
                        )
                    })
                }
            }
            return@withContext Result.Error()
        }
}