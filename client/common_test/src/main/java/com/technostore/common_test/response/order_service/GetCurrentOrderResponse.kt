package com.technostore.common_test.response.order_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object GetCurrentOrderResponse {
    @Language("JSON")
    const val success = """
        {
          "id": 1,
          "products": [
            {
              "id": ${TestData.PRODUCT_ID},
              "linkPhoto": "${TestData.PRODUCT_PHOTO_LINK}",
              "price": ${TestData.PRODUCT_PRICE},
              "name": "${TestData.PRODUCT_NAME}", 
              "rating": ${TestData.PRODUCT_RATING}, 
              "count": ${TestData.PRODUCT_COUNT}
            }
          ]
        }
    """
}