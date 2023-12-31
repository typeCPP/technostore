package com.technostore.feature_profile.business

import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.model.ProfileModel

interface ProfileRepository {

    suspend fun getProfile(): Result<ProfileModel>
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit>
    suspend fun editProfile(name: String, lastName: String, byteArray: ByteArray?): Result<Unit>
    suspend fun logout()
}