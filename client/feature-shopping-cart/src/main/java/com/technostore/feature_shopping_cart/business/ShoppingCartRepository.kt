package com.technostore.feature_shopping_cart.business

import com.technostore.arch.result.Result
import com.technostore.feature_shopping_cart.business.model.OrderDetailModel

interface ShoppingCartRepository {
    suspend fun getCurrentOrder(): Result<OrderDetailModel>

    suspend fun makeOrderCompleted(id: Long): Result<Unit>

    suspend fun setProductCount(productId: Long, count: Int): Result<Unit>
}