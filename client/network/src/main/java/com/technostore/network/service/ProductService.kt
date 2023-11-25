package com.technostore.network.service

import com.technostore.network.model.product.response.CategoryResponse
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {
    @GET("${URL.PRODUCT_SERVICE_BASE_URL}/popular-categories")
    suspend fun getPopularCategories(): Response<List<CategoryResponse>>
}