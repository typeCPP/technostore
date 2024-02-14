package com.technostore.common_test.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.technostore.common_test.response.review_service.ReviewsByProductIdResponse
import com.technostore.common_test.response.review_service.UserReviewByProductIdResponse
import com.technostore.network.utils.URL
import java.net.HttpURLConnection

object ReviewServiceMock {
    val setReview = SetReview()
    val userReviewByProductId = UserReviewByProductId()
    val reviewsByProductId = ReviewsByProductId()

    inline operator fun invoke(block: ReviewServiceMock.() -> Unit) = apply(block)

    class SetReview internal constructor() {

        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/${URL.REVIEW_SERVICE_BASE_URL}/review/newReview"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
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

    class UserReviewByProductId internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/${URL.REVIEW_SERVICE_BASE_URL}/review/by-product-id/.+"))

        fun success() {
            stubFor(matcher.willReturn(ok(UserReviewByProductIdResponse.success)))
        }

        fun emptyBody() {
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

    class ReviewsByProductId internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/${URL.REVIEW_SERVICE_BASE_URL}/review/all-by-product-id/.+"))

        fun success() {
            stubFor(matcher.willReturn(ok(ReviewsByProductIdResponse.success)))
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
}