package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.feature_product.business.model.ProductDetailModel
import com.technostore.feature_product.business.model.mapper.ProductDetailMapper
import com.technostore.network.service.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val productService: ProductService,
    private val productDetailMapper: ProductDetailMapper
) : ProductRepository {
    override suspend fun getProductById(id: Long): Result<ProductDetailModel> =
        withContext(Dispatchers.IO) {
            val response = productService.getProductById(id)
            if (response.isSuccessful && response.body() != null) {
                return@withContext Result.Success(
                    productDetailMapper.mapFromResponseToModel(
                        response.body()!!
                    )
                )
            }
            return@withContext Result.Error()
        }
}