package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.feature_product.business.model.ProductDetailModel

interface ProductRepository {
    suspend fun getProductById(id: Long): Result<ProductDetailModel>
}