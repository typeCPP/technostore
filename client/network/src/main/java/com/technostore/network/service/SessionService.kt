package com.technostore.network.service

import com.technostore.network.model.session.request.RefreshTokenRequest
import com.technostore.network.model.session.response.RefreshTokenResponse
import com.technostore.network.utils.URL
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionService {
    @POST("${URL.USER_SERVICE_BASE_URL}/refresh-tokens")
    fun refreshTokens(
        @Body refreshTokenResponse: RefreshTokenRequest
    ): Call<RefreshTokenResponse>
}