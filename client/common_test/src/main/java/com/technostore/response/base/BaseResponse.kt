package com.technostore.response.base

import org.intellij.lang.annotations.Language

object BaseResponse {
    @Language("JSON")
    fun customError(message: String) = """
        {
        "message": "$message"
        }
    """
}