package com.technostore.common_test.response.product_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object CategoryResponse {
    @Language("JSON")
    const val success = """
        [
            {
                "id": ${TestData.FIRST_CATEGORY_ID},
                "name": "${TestData.FIRST_CATEGORY_NAME}"
            },
            {
                "id": ${TestData.SECOND_CATEGORY_ID},
                "name": "${TestData.SECOND_CATEGORY_NAME}"
            }
        ]
    """
}