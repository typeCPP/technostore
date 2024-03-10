package com.technostore.network.service

import com.technostore.network.model.session.request.RefreshTokenRequest
import com.technostore.network.model.session.response.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionService {
    @POST("refresh-tokens")
    fun refreshTokens(
        @Body refreshTokenResponse: RefreshTokenRequest
    ): Call<RefreshTokenResponse>
}