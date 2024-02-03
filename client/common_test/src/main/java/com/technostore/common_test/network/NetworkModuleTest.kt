package com.technostore.common_test.network

import com.technostore.common_test.MockServer
import com.technostore.network.service.LoginService
import com.technostore.network.service.SessionService
import com.technostore.network.service.UserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModuleTest {
    private val okkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
        )
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(MockServer.URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okkHttpClient)
        .build()

    val loginService: LoginService = retrofit.create(LoginService::class.java)
    val sessionService: SessionService = retrofit.create(SessionService::class.java)
    val userService: UserService = retrofit.create(UserService::class.java)
}