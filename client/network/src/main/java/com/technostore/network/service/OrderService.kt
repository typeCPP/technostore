package com.technostore.network.service

import com.technostore.network.model.order.response.Order
import com.technostore.network.model.order.response.OrderDetailResponse
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    @GET("${URL.ORDER_SERVICE_BASE_URL}/order/get-completed-orders")
    suspend fun getCompletedOrders(): Response<Order>

    @GET("${URL.ORDER_SERVICE_BASE_URL}/order/get-completed-order/{id}")
    suspend fun getCompletedOrderById(@Path("id") id: Long): Response<OrderDetailResponse>

    @GET("${URL.ORDER_SERVICE_BASE_URL}/order/get-current-order")
    suspend fun getCurrentOrder(): Response<OrderDetailResponse>

    @POST("${URL.ORDER_SERVICE_BASE_URL}/order/complete-order/{id}")
    suspend fun makeOrderCompleted(@Path("id") id: Long): Response<Unit>

    @POST("${URL.ORDER_SERVICE_BASE_URL}/order/set-product-count")
    suspend fun setProductCount(
        @Query("productId") productId: Long,
        @Query("count") count: Int
    ): Response<Unit>
}