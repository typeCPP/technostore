package com.technostore.common_test.response.user_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object ProfileResponse {
    @Language("JSON")
    const val success = """
        {
          "name": "${TestData.NAME}",
          "lastName": "${TestData.LAST_NAME}",
          "image": "${TestData.USER_PHOTO_LINK}",
          "email": "${TestData.EMAIL}"
        }
    """
}