package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.feature_product.business.model.ProductDetailModel
import com.technostore.feature_product.business.model.ReviewModel
import com.technostore.feature_product.business.model.UserReviewModel

interface ProductRepository {
    suspend fun getProductById(id: Long): Result<ProductDetailModel>
    suspend fun setReview(productId: Long, rating: Int, text: String?): Result<Unit>
    suspend fun setProductCount(productId: Long, count: Int): Result<Unit>
    suspend fun getUserReview(productId: Long): Result<UserReviewModel?>
    suspend fun getReviews(productId: Long): Result<List<ReviewModel>>
}