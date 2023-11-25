package com.technostore.network.interceptor

import com.technostore.app_store.store.AppStore
import com.technostore.network.model.session.request.RefreshTokenRequest
import com.technostore.network.service.SessionService
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Provider

class ConnectionInterceptor @Inject constructor(
    private val sessionServiceProvider: Provider<SessionService>,
    private val appStoreProvider: Provider<AppStore>
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            return setHeader(response, chain)
        } catch (e: Exception) {
            e.printStackTrace()
            val msg = when (e) {
                is ConnectException -> "Connection exception"
                is UnknownHostException -> "Unknown host exception"
                else -> e.message
            }

            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(999)
                .message(msg.orEmpty())
                .body("{${e}}".toResponseBody(null))
                .build()
        }
    }

    private fun setHeader(response: Response, chain: Interceptor.Chain): Response {
        val appStore = appStoreProvider.get()
        if (response.code == 403 || response.code == 401) {
            val sessionService = sessionServiceProvider.get()
            if (!appStore.accessTokenIsValid()) {
                if (!appStore.refreshTokenIsValid()) {
                    val refreshTokenResponse = RefreshTokenRequest(
                        generateRefreshToken = true,
                        email = appStore.email.orEmpty(),
                        accessToken = appStore.accessToken.orEmpty(),
                        refreshToken = appStore.refreshToken.orEmpty()
                    )
                    val tokenResponse = sessionService
                        .refreshTokens(refreshTokenResponse)
                        .execute()
                        .body()
                    tokenResponse?.let {
                        appStore.refreshAccessToken(
                            tokenResponse.accessToken,
                            it.expireTimeAccessToken
                        )
                        tokenResponse.refreshToken?.let { refreshToken ->
                            tokenResponse.expireTimeRefreshToken?.let { expireTimeRefreshToken ->
                                appStore.refreshRefreshToken(
                                    refreshToken,
                                    expireTimeRefreshToken
                                )
                            }
                        }
                    }
                } else {
                    val refreshTokenResponse = RefreshTokenRequest(
                        generateRefreshToken = false,
                        email = appStore.email.orEmpty(),
                        accessToken = appStore.accessToken.orEmpty(),
                        refreshToken = appStore.refreshToken.orEmpty()
                    )
                    val tokenResponse = sessionService
                        .refreshTokens(refreshTokenResponse)
                        .execute()
                        .body()
                    tokenResponse?.let {
                        appStore.refreshAccessToken(
                            tokenResponse.accessToken,
                            it.expireTimeAccessToken
                        )
                    }
                }
            }
            response.close()
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${appStore.accessToken}")
                .build()
            return chain.proceed(newRequest)
        }
        return response.newBuilder()
            .body(response.body)
            .build()
    }
}