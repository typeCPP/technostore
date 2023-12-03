package com.technostore.feature_order.business

import com.technostore.arch.result.Result
import com.technostore.feature_order.business.model.OrderDetailModel
import com.technostore.network.model.order.response.Order

interface OrderRepository {
    suspend fun getCompletedOrders(): Result<Order>
    suspend fun getCompletedOrderById(id: Long): Result<OrderDetailModel>
}