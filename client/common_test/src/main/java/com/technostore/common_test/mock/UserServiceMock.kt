package com.technostore.common_test.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.technostore.common_test.response.user_service.ProfileResponse
import com.technostore.network.utils.URL
import java.net.HttpURLConnection

object UserServiceMock {

    val changePassword = ChangePassword()
    val profile = Profile()
    val editProfile = EditProfile()
    val logout = Logout()

    inline operator fun invoke(block: UserServiceMock.() -> Unit) = apply(block)

    class ChangePassword internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/${URL.USER_SERVICE_BASE_URL}/user/change-password"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun notFound() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_NOT_FOUND)
            stubFor(matcher.willReturn(response))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class Profile internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/${URL.USER_SERVICE_BASE_URL}/user/profile"))

        fun success() {
            stubFor(matcher.willReturn(ok(ProfileResponse.success)))
        }

        fun emptyBody() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class EditProfile internal constructor() {
        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/${URL.USER_SERVICE_BASE_URL}/user/edit-profile"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class Logout internal constructor() {
        val pattern = urlPathMatching("/${URL.USER_SERVICE_BASE_URL}/user/logout")
        private val matcher: MappingBuilder
            get() = get(pattern)

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }
}