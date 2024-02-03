import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching
import com.technostore.network.utils.URL.USER_SERVICE_BASE_URL
import com.technostore.response.base.BaseResponse
import com.technostore.response.login_service.LoginResponse
import java.net.HttpURLConnection

object LoginServiceMock {

    val login get() = Login()

    inline operator fun invoke(block: LoginServiceMock.() -> Unit) = apply(block)

    class Login internal constructor() {

        private val matcher: MappingBuilder
            get() = post(urlPathMatching("/$USER_SERVICE_BASE_URL/user/login"))

        fun success() {
            stubFor(matcher.willReturn(ok(LoginResponse.success)))
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
    }
}
