package com.technostore.feature_main_page.business

import com.technostore.network.service.ProductService
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.shared_search.business.model.mapper.ProductSearchMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.technostore.arch.result.Result
import com.technostore.shared_search.business.SEARCH_NOTHING

private const val PAGE_SIZE = 100

class MainRepositoryImpl(
    private val productService: ProductService,
    private val productSearchMapper: ProductSearchMapper
) : MainRepository {
    override suspend fun searchByPopularity(): Result<List<ProductSearchModel>> =
        withContext(Dispatchers.IO) {
            val response = productService.searchProducts(
                numberPage = 0,
                sizePage = PAGE_SIZE,
                word = "",
                sort = SEARCH_NOTHING,
                minRating = 0,
                maxRating = 10,
                minPrice = 0,
                maxPrice = 1000000,
                categories = null,
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return@withContext Result.Success(body.listOfProducts.map {
                        productSearchMapper.mapFromResponseToModel(
                            it
                        )
                    })
                }
            }
            return@withContext Result.Error()
        }

    override suspend fun searchByCategory(categoryId: Long): Result<List<ProductSearchModel>> =
        withContext(Dispatchers.IO) {
            val response = productService.searchProducts(
                numberPage = 0,
                sizePage = PAGE_SIZE,
                word = null,
                sort = SEARCH_NOTHING,
                minRating = null,
                maxRating = null,
                minPrice = null,
                maxPrice = null,
                categories = categoryId.toString(),
            )
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return@withContext Result.Success(body.listOfProducts.map {
                        productSearchMapper.mapFromResponseToModel(
                            it
                        )
                    })
                }
            }
            return@withContext Result.Error()
        }
}
