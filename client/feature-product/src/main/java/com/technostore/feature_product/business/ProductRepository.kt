package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.network.model.product.response.ProductDetail

interface ProductRepository {

    suspend fun getProductById(id: Long): Result<ProductDetail>
}