package com.technostore.shared_search.business

import com.technostore.arch.result.Result
import com.technostore.network.service.ProductService
import com.technostore.shared_search.business.model.Category
import com.technostore.shared_search.business.model.CategoryWithCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedSearchRepositoryImpl(
    private val productService: ProductService
) : SharedSearchRepository {

    @Volatile
    private var selectedCategories: ArrayList<Long> = arrayListOf()

    @Volatile
    private var isSelectByPopularity: Boolean = false

    @Volatile
    private var isSelectByRating: Boolean = false

    @Volatile
    private var minCost: Float = 1f

    @Volatile
    private var maxCost: Float = 1000000f

    @Volatile
    private var minRating: Float = 1f

    @Volatile
    private var maxRating: Float = 10f

    override suspend fun getCategories(): Result<List<CategoryWithCheck>> =
        withContext(Dispatchers.IO) {
            val response = productService.getPopularCategories()
            if (response.isSuccessful) {
                val categories = response.body()
                if (categories != null) {
                    val categoriesModel = categories.map {
                        CategoryWithCheck(
                            Category(id = it.id, name = it.name),
                            isSelected = false
                        )
                    }
                    return@withContext Result.Success(categoriesModel)
                }
            }
            return@withContext Result.Error()
        }

    override suspend fun setIsSelectByPopularity():Boolean {
        isSelectByPopularity = !isSelectByPopularity
        return isSelectByPopularity
    }

    override suspend fun setIsSelectByRating():Boolean {
        isSelectByRating = !isSelectByRating
        return isSelectByRating
    }

    override suspend fun updateSelectedCategories(categoryWithCheck: CategoryWithCheck) {
        if (categoryWithCheck.isSelected) {
            selectedCategories.add(categoryWithCheck.category.id)
        } else {
            selectedCategories.removeIf { it == categoryWithCheck.category.id }
        }
    }

    override suspend fun updateCostBoundaries(minCost: Float, maxCost: Float) {
        this.minCost = minCost
        this.maxCost = maxCost
    }


    override suspend fun updateRatingBoundaries(minRating: Float, maxRating: Float) {
        this.minRating = minRating
        this.maxRating = maxRating
    }
}