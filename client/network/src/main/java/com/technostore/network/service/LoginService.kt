package com.technostore.network.service

import com.technostore.network.model.login.response.EmailExistsResponse
import com.technostore.network.model.login.response.LoginResponse
import com.technostore.network.model.login.request.LoginRequest
import com.technostore.network.utils.URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface LoginService {
    @Headers("Accept: application/json")
    @POST("user/login")
    suspend fun signIn(@Body data: LoginRequest): Response<LoginResponse>

    @GET("user/check-email-exists")
    suspend fun checkEmailExists(
        @Query("email", encoded = true) email: String
    ): Response<EmailExistsResponse>

    @Multipart
    @POST("user/registration")
    suspend fun signUp(
        @Part("registerBeanString") data: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<LoginResponse>

    @GET("user/confirm-account")
    suspend fun checkRecoveryCodeForAccountConfirmations(
        @Query("confirmationCode") code: String,
        @Query("email", encoded = true) email: String
    ): Response<Unit>

    @GET("user/password-recovery")
    suspend fun checkRecoveryCode(
        @Query("code") code: String,
        @Query("email", encoded = true) email: String
    ): Response<LoginResponse>

    @GET("send-code-for-recovery-password")
    suspend fun sendCodeForRecoveryPassword(
        @Query("email", encoded = true) email: String
    ): Response<Unit>

    @GET("send-code-for-confirmation-account")
    suspend fun sendCodeForAccountConfirmations(@Query("userId") userId: Long): Response<Unit>
}