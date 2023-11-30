package com.technostore.feature_shopping_cart.business

import com.technostore.arch.result.Result
import com.technostore.feature_shopping_cart.business.model.OrderDetailModel
import com.technostore.feature_shopping_cart.business.model.mapper.OrderDetailMapper
import com.technostore.network.service.OrderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShoppingCartRepositoryImpl(
    private val orderService: OrderService,
    private val orderDetailMapper: OrderDetailMapper
) : ShoppingCartRepository {
    override suspend fun getCurrentOrder(): Result<OrderDetailModel> = withContext(Dispatchers.IO) {
        val result = orderService.getCurrentOrder()
        if (result.isSuccessful && result.body() != null) {
            return@withContext Result.Success(
                orderDetailMapper.mapFromResponseToModel(
                    result.body()!!
                )
            )
        }
        return@withContext Result.Error()
    }

    override suspend fun makeOrderCompleted(id: Long): Result<Unit> = withContext(Dispatchers.IO) {
        val result = orderService.makeOrderCompleted(id)
        if (result.isSuccessful) {
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
}