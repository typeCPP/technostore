package com.technostore.network.service

import com.technostore.network.model.review.response.ReviewResponse
import com.technostore.network.model.review.response.UserReviewResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewService {
    @GET("review/all-by-product-id/{id}")
    suspend fun getReviewsByProductId(@Path("id") id: Long): Response<List<ReviewResponse>>

    @POST("review/newReview")
    suspend fun setReview(
        @Query("productId") productId: Long,
        @Query("text") text: String?,
        @Query("rate") rate: Int
    ): Response<Unit>

    @GET("review/by-product-id/{id}")
    suspend fun getUserReviewByProductId(@Path("id") id: Long): Response<UserReviewResponse>
}