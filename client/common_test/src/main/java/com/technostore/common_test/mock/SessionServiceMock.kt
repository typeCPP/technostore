package com.technostore.common_test.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.technostore.common_test.response.session_service.RefreshTokenResponse
import com.technostore.network.utils.URL
import java.net.HttpURLConnection

object SessionServiceMock {

    val refreshTokens = RefreshTokens()

    inline operator fun invoke(block: SessionServiceMock.() -> Unit) = apply(block)

    class RefreshTokens internal constructor() {
        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/${URL.USER_SERVICE_BASE_URL}/refresh-tokens"))

        fun success() {
            WireMock.stubFor(matcher.willReturn(ok(RefreshTokenResponse.success)))
        }

        fun successRefreshTokenNull() {
            WireMock.stubFor(matcher.willReturn(ok(RefreshTokenResponse.successRefreshTokenNull)))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            WireMock.stubFor(matcher.willReturn(response))
        }
    }
}