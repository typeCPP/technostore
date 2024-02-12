package com.technostore.common_test.response.order_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object CompletedOrdersResponse {
    @Language("JSON")
    const val success = """
        {
          "ids": [
          ${TestData.FIRST_ORDER_ID},
          ${TestData.SECOND_ORDER_ID}
          ]
        }
    """
}