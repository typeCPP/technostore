package com.technostore.network.service

import com.technostore.network.model.request.LoginRequest
import com.technostore.network.model.response.LoginResponse
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers("Accept: application/json")
    @POST("${URL.USER_SERVICE_BASE_URL}/user/login")
    suspend fun signIn(@Body data: LoginResponse): Response<LoginRequest>
}