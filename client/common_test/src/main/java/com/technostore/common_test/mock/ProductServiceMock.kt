package com.technostore.common_test.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.technostore.common_test.response.product_service.CategoryResponse
import com.technostore.common_test.response.product_service.ProductSearchResultResponse
import com.technostore.network.utils.URL
import java.net.HttpURLConnection

object ProductServiceMock {
    val popularCategories = PopularCategories()
    val searchProducts = SearchProducts()

    inline operator fun invoke(block: ProductServiceMock.() -> Unit) = apply(block)

    class PopularCategories internal constructor() {

        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/${URL.PRODUCT_SERVICE_BASE_URL}/product/popular-categories"))

        fun success() {
            stubFor(matcher.willReturn(ok(CategoryResponse.success)))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
            stubFor(matcher.willReturn(response))
        }
    }

    class SearchProducts internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/${URL.PRODUCT_SERVICE_BASE_URL}/product/search"))

        fun success() {
            stubFor(matcher.willReturn(ok(ProductSearchResultResponse.success)))
        }

        fun empty() {
            stubFor(matcher.willReturn(ok(ProductSearchResultResponse.empty)))
        }

        fun internalError() {
            val response = WireMock.aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
            stubFor(matcher.willReturn(response))
        }
    }
}