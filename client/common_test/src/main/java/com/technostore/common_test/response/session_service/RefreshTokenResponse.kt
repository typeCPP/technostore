package com.technostore.common_test.response.session_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object RefreshTokenResponse {
    @Language("JSON")
    const val success = """
        {
          "email": "${TestData.EMAIL}",
          "access-token": "${TestData.ACCESS_TOKEN}",
          "expireTimeAccessToken": "${TestData.EXPIRE_TIME_ACCESS_TOKEN}",
          "refresh-token": "${TestData.REFRESH_TOKEN}",
          "expireTimeRefreshToken": "${TestData.EXPIRE_TIME_REFRESH_TOKEN}"
        }
    """
    @Language("JSON")
    const val successRefreshTokenNull = """
        {
          "email": "${TestData.EMAIL}",
          "access-token": "${TestData.ACCESS_TOKEN}",
          "expireTimeAccessToken": "${TestData.EXPIRE_TIME_ACCESS_TOKEN}"
        }
    """
}