package com.technostore.feature_profile.business

import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.model.OrderDetailModel
import com.technostore.feature_profile.business.model.ProfileModel
import com.technostore.network.model.order.response.Order

interface ProfileRepository {

    suspend fun getProfile(): Result<ProfileModel>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>
    suspend fun editProfile(name: String, lastName: String, byteArray: ByteArray?): Result<Unit>
    suspend fun getCompletedOrders():Result<Order>
    suspend fun logout()
    suspend fun getCompletedOrderById(id: Long): Result<OrderDetailModel>
}