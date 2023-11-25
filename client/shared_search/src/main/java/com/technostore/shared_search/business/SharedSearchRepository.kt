package com.technostore.shared_search.business

import com.technostore.arch.result.Result
import com.technostore.shared_search.business.model.CategoryWithCheck

interface SharedSearchRepository {

    suspend fun getCategories(): Result<List<CategoryWithCheck>>
    suspend fun setIsSelectByPopularity(): Boolean
    suspend fun setIsSelectByRating(): Boolean
    suspend fun updateSelectedCategories(categoryWithCheck: CategoryWithCheck)
    suspend fun updateCostBoundaries(minCost: Float, maxCost: Float)
    suspend fun updateRatingBoundaries(minRating: Float, maxRating: Float)
}