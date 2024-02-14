package com.technostore.common_test.response.base

import org.intellij.lang.annotations.Language

object BaseResponse {
    @Language("JSON")
    fun customError(message: String) = """
        {
        "message": "$message"
        }
    """

    @Language("JSON")
    const val emptyBody = """
        {
        }
    """
}