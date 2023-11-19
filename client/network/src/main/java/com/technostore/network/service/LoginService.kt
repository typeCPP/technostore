package com.technostore.network.service

import com.technostore.network.model.login.response.EmailExistsResponse
import com.technostore.network.model.login.response.LoginResponse
import com.technostore.network.model.login.request.LoginRequest
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @Headers("Accept: application/json")
    @POST("${URL.USER_SERVICE_BASE_URL}/user/login")
    suspend fun signIn(@Body data: LoginRequest): Response<LoginResponse>

    @GET("${URL.USER_SERVICE_BASE_URL}/user/check-email-exists")
    suspend fun checkEmailExists(
        @Query("email", encoded = true) email: String
    ): Response<EmailExistsResponse>
}