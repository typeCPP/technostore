package com.technostore.network.service

import com.technostore.network.model.order.response.Order
import com.technostore.network.model.order.response.OrderDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    @GET("order/get-completed-orders")
    suspend fun getCompletedOrders(): Response<Order>

    @GET("order/get-completed-order/{id}")
    suspend fun getCompletedOrderById(@Path("id") id: Long): Response<OrderDetailResponse>

    @GET("order/get-current-order")
    suspend fun getCurrentOrder(): Response<OrderDetailResponse>

    @POST("order/complete-order/{id}")
    suspend fun makeOrderCompleted(@Path("id") id: Long): Response<Unit>

    @POST("order/set-product-count")
    suspend fun setProductCount(
        @Query("productId") productId: Long,
        @Query("count") count: Int
    ): Response<Unit>
}