package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.feature_product.business.model.ProductDetailModel
import com.technostore.feature_product.business.model.ReviewModel
import com.technostore.feature_product.business.model.UserReviewModel
import com.technostore.feature_product.business.model.mapper.ProductDetailMapper
import com.technostore.feature_product.business.model.mapper.ReviewMapper
import com.technostore.feature_product.business.model.mapper.UserReviewMapper
import com.technostore.network.service.OrderService
import com.technostore.network.service.ProductService
import com.technostore.network.service.ReviewService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val productService: ProductService,
    private val reviewService: ReviewService,
    private val orderService: OrderService,
    private val productDetailMapper: ProductDetailMapper,
    private val userReviewMapper: UserReviewMapper,
    private val reviewMapper: ReviewMapper
) : ProductRepository {
    override suspend fun getProductById(id: Long): Result<ProductDetailModel> =
        withContext(Dispatchers.IO) {
            val response = productService.getProductById(id)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return@withContext Result.Success(productDetailMapper.mapFromResponseToModel(body))
            }
            return@withContext Result.Error()
        }

    override suspend fun setReview(productId: Long, rating: Int, text: String?): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = reviewService.setReview(productId, text, rating)
            if (response.isSuccessful) {
                return@withContext Result.Success()
            }
            return@withContext Result.Error()
        }

    override suspend fun setProductCount(productId: Long, count: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            val result = orderService.setProductCount(productId, count)
            if (result.isSuccessful) {
                return@withContext Result.Success()
            }
            return@withContext Result.Error()
        }

    override suspend fun getUserReview(productId: Long): Result<UserReviewModel?> =
        withContext(Dispatchers.IO) {
            val result = reviewService.getUserReviewByProductId(productId)
            if (result.isSuccessful) {
                val body = result.body()
                if (body != null) {
                    return@withContext Result.Success(userReviewMapper.mapFromResponseToModel(body))
                }
            }
            if (result.code() == 404) {
                return@withContext Result.Success()
            }
            return@withContext Result.Error()
        }

    override suspend fun getReviews(productId: Long): Result<List<ReviewModel>> =
        withContext(Dispatchers.IO) {
            val response = reviewService.getReviewsByProductId(productId)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                return@withContext Result.Success(
                    body.map { reviewMapper.mapFromResponseToModel(it) })
            }
            return@withContext Result.Error()
        }
}
