package com.technostore.feature_order.business

import com.technostore.arch.result.Result
import com.technostore.feature_order.business.model.OrderDetailModel
import com.technostore.feature_order.business.model.mapper.OrderDetailMapper
import com.technostore.network.model.order.response.Order
import com.technostore.network.service.OrderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepositoryImpl(
    private val orderService: OrderService,
    private val orderDetailMapper: OrderDetailMapper
) : OrderRepository {
    override suspend fun getCompletedOrders(): Result<Order> = withContext(Dispatchers.IO) {
        val orderResponse = orderService.getCompletedOrders()
        val body = orderResponse.body()
        if (orderResponse.isSuccessful && body != null) {
            return@withContext Result.Success(body)
        }
        return@withContext Result.Error()
    }

    override suspend fun getCompletedOrderById(id: Long): Result<OrderDetailModel> =
        withContext(Dispatchers.IO) {
            val orderResponse = orderService.getCompletedOrderById(id)
            val body = orderResponse.body()
            if (orderResponse.isSuccessful && body != null) {
                return@withContext Result.Success(
                    orderDetailMapper.mapFromResponseToModel(body)
                )
            }
            return@withContext Result.Error()
        }
}