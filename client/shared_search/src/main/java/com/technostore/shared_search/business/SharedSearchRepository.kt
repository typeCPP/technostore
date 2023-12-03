package com.technostore.shared_search.business

import com.technostore.arch.result.Result
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.business.model.ProductSearchModel

interface SharedSearchRepository {

    suspend fun getCategories(): Result<List<CategoryWithCheck>>
    fun clearNumberPage()
    fun getMinPrice(): Float
    fun getMaxPrice(): Float
    fun getMinRating(): Float
    fun getMaxRating(): Float
    fun getIsSortByPopularity(): Boolean
    fun getIsSortByRating(): Boolean
    fun getSelectedCategories(): ArrayList<Long>
    suspend fun setIsSelectByPopularity(): Boolean
    suspend fun setIsSelectByRating(): Boolean
    suspend fun updateSelectedCategories(categoryWithCheck: CategoryWithCheck)
    suspend fun updateCostBoundaries(minCost: Float, maxCost: Float)
    suspend fun updateRatingBoundaries(minRating: Float, maxRating: Float)
    suspend fun setProductCount(productId: Long, count: Int): Result<Unit>

    suspend fun searchProducts(word: String): Result<List<ProductSearchModel>>
}