package com.technostore.common_test.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.technostore.common_test.response.order_service.GetCurrentOrderResponse
import com.technostore.network.utils.URL
import java.net.HttpURLConnection

object OrderServiceMock {

    val getCurrentOrder = GetCurrentOrder()
    val completeOrder = CompleteOrder()
    val getCompletedOrder = GetCompletedOrder()
    val setProductCount = SetProductCount()

    inline operator fun invoke(block: OrderServiceMock.() -> Unit) = apply(block)

    class GetCurrentOrder internal constructor() {

        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/${URL.ORDER_SERVICE_BASE_URL}/order/get-current-order"))

        fun success() {
            stubFor(matcher.willReturn(ok(GetCurrentOrderResponse.success)))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class CompleteOrder internal constructor() {
        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/${URL.ORDER_SERVICE_BASE_URL}/order/complete-order/.+"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class GetCompletedOrder internal constructor() {
        val urlPattern = urlPathMatching("/${URL.ORDER_SERVICE_BASE_URL}/order/get-completed-order/.+")
        private val matcher: MappingBuilder get() = get(urlPattern)

        fun success() {
            stubFor(matcher.willReturn(ok(GetCurrentOrderResponse.success)))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class SetProductCount internal constructor() {
        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/${URL.ORDER_SERVICE_BASE_URL}/order/set-product-count"))

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
