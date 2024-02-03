package com.technostore.common_test.response.login_service

import org.intellij.lang.annotations.Language

object CheckEmailExistsResponse {
    @Language("JSON")
    const val successExists="""
        {
          "exists": true
        }
    """
    @Language("JSON")
    const val successNotExists="""
        {
          "exists": false
        }
    """
}