package com.technostore.common_test.response.login_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language


object LoginResponse {
    @Language("JSON")
    const val success = """
        {
          "id": ${TestData.ID},
          "email": "${TestData.EMAIL}",
          "access-token": "${TestData.ACCESS_TOKEN}",
          "expireTimeAccessToken": "${TestData.EXPIRE_TIME_ACCESS_TOKEN}",
          "refresh-token": "${TestData.REFRESH_TOKEN}",
          "expireTimeRefreshToken": "${TestData.EXPIRE_TIME_REFRESH_TOKEN}"
        }
    """
}