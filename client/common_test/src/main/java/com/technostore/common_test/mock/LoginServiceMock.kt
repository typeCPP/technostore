package com.technostore.common_test.mock

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.technostore.common_test.response.base.BaseResponse
import com.technostore.common_test.response.login_service.CheckEmailExistsResponse
import com.technostore.common_test.response.login_service.LoginResponse
import com.technostore.common_test.response.login_service.PasswordRecoveryResponse
import com.technostore.common_test.response.login_service.RegistrationResponse
import java.net.HttpURLConnection

object LoginServiceMock {

    val login get() = Login()
    val checkEmailExists = CheckEmailExists()
    val registration = Registration()
    val confirmAccount = ConfirmAccount()
    val passwordRecovery = PasswordRecovery()
    val sendCodeForRecoveryPassword = SendCodeForRecoveryPassword()
    val sendCodeForConfirmationAccount = SendCodeForConfirmationAccount()
    val changePassword = ChangePassword()

    inline operator fun invoke(block: LoginServiceMock.() -> Unit) = apply(block)

    class Login internal constructor() {

        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/user/login"))

        fun success() {
            stubFor(matcher.willReturn(ok(LoginResponse.success)))
        }

        fun emptyBody() {
            stubFor(matcher.willReturn(ok()))
        }

        fun errorPassword(message: String) {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_NOT_FOUND)
                .withBody(BaseResponse.customError(message))
            stubFor(matcher.willReturn(response))
        }

        fun errorEmail(message: String) {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_NOT_FOUND)
                .withBody(BaseResponse.customError(message))
            stubFor(matcher.willReturn(response))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class CheckEmailExists internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/user/check-email-exists"))

        fun successExists() {
            stubFor(matcher.willReturn(ok(CheckEmailExistsResponse.successExists)))
        }

        fun emptyBody() {
            stubFor(matcher.willReturn(ok()))
        }

        fun successNotExists() {
            stubFor(matcher.willReturn(ok(CheckEmailExistsResponse.successNotExists)))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class Registration internal constructor() {
        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/user/registration"))

        fun success() {
            stubFor(matcher.willReturn(ok(RegistrationResponse.success)))
        }

        fun emptyBody() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class ChangePassword internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/user/change-password"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class ConfirmAccount internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/user/confirm-account"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }

        fun notFound() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_NOT_FOUND)
            stubFor(matcher.willReturn(response))
        }
    }

    class PasswordRecovery internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/user/password-recovery"))

        fun success() {
            stubFor(matcher.willReturn(ok(PasswordRecoveryResponse.success)))
        }

        fun emptyBody() {
            stubFor(matcher.willReturn(ok()))
        }

        fun conflict() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_CONFLICT)
            stubFor(matcher.willReturn(response))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class SendCodeForRecoveryPassword internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/send-code-for-recovery-password"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }

    class SendCodeForConfirmationAccount internal constructor() {
        private val matcher: MappingBuilder
            get() = get(urlPathMatching("/send-code-for-confirmation-account"))

        fun success() {
            stubFor(matcher.willReturn(ok()))
        }

        fun internalError() {
            val response = aResponse()
                .withStatus(HttpURLConnection.HTTP_INTERNAL_ERROR)
            stubFor(matcher.willReturn(response))
        }
    }
}
