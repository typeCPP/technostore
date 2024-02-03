package com.technostore

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.Options
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule


class MockServer private constructor(options: Options) : WireMockServer(options) {

    companion object {
        private const val HOST = "127.0.0.1"
        private const val PORT = 8080
        const val URL = "http://$HOST:$PORT/"

        fun testRule() = WireMockRule(
            WireMockConfiguration.wireMockConfig()
                .bindAddress(HOST)
                .port(PORT)
        )
    }

    override fun start() {
        WireMock.configureFor(client)
        super.start()
    }
}