package com.technostore.network.interceptor

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.ConnectException
import java.net.UnknownHostException

class ConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            val bodyString = response.body!!.string()
            return response.newBuilder()
                .body(bodyString.toResponseBody(response.body?.contentType()))
                .build()
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
}