package com.technostore.network.service

import com.technostore.network.model.user.response.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserService {
    @GET("user/change-password")
    suspend fun changePassword(
        @Query("newPassword") newPassword: String,
        @Query("oldPassword") oldPassword: String?,
        @Query("refreshToken", encoded = true) refreshToken: String
    ): Response<Unit>

    @GET("user/profile")
    suspend fun getProfile(): Response<UserProfileResponse>

    @Multipart
    @POST("user/edit-profile")
    suspend fun editProfile(
        @Part("editUserBeanString") data: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<Unit>

    @GET("user/logout")
    suspend fun logout(
        @Query("refreshToken", encoded = true) refreshToken: String
    ): Response<Unit>
}