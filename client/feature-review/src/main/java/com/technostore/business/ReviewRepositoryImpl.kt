package com.technostore.business

import com.technostore.arch.result.Result
import com.technostore.business.model.mapper.ReviewMapper
import com.technostore.business.model.ReviewModel
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
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.Success(
                    response.body()!!.map { reviewMapper.mapFromResponseToModel(it) })
            }
            return@withContext Result.Error()
        }
}