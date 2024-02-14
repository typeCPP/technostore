package com.technostore.feature_review.business

import com.technostore.arch.result.Result
import com.technostore.feature_review.business.model.ReviewModel

interface ReviewRepository {
    suspend fun getReviews(productId: Long): Result<List<ReviewModel>>
}