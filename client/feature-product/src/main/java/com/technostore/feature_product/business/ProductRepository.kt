package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.feature_product.business.model.ProductDetailModel

interface ProductRepository {
    suspend fun getProductById(id: Long): Result<ProductDetailModel>
    suspend fun setReview(productId: Long, rating: Int, text: String?): Result<Unit>
}