package com.technostore.feature_main_page.business

import com.technostore.arch.result.Result
import com.technostore.shared_search.business.model.ProductSearchModel

interface MainRepository {
    suspend fun searchByPopularity(): Result<List<ProductSearchModel>>
    suspend fun searchByCategory(categoryId:Long): Result<List<ProductSearchModel>>
}