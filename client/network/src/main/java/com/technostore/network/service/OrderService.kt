package com.technostore.network.service

import com.technostore.network.model.order.response.Order
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.GET

interface OrderService {
    @GET("${URL.ORDER_SERVICE_BASE_URL}/order/get-completed-orders")
    suspend fun getCompletedOrders(): Response<Order>
}