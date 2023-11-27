package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.network.model.product.response.ProductDetail
import com.technostore.network.service.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val productService: ProductService
) : ProductRepository {
    override suspend fun getProductById(id: Long): Result<ProductDetail> =
        withContext(Dispatchers.IO) {
            val response = productService.getProductById(id)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.Success(response.body())
            }
            return@withContext Result.Error()
        }
}