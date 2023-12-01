package com.technostore.business

import com.technostore.arch.result.Result
import com.technostore.business.model.ReviewModel

interface ReviewRepository {
    suspend fun getReviews(productId: Long): Result<List<ReviewModel>>
}