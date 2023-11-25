package com.technostore.network.service

import com.technostore.network.model.user.response.UserProfileResponse
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("${URL.USER_SERVICE_BASE_URL}/user/change-password")
    suspend fun changePassword(
        @Query("newPassword") newPassword: String,
        @Query("oldPassword") oldPassword: String?,
        @Query("refreshToken", encoded = true) refreshToken: String
    ): Response<Unit>

    @GET("${URL.USER_SERVICE_BASE_URL}/user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>
}