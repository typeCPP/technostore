package com.technostore.feature_profile.business

import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.model.ProfileModel
import com.technostore.network.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(private val userService: UserService) : ProfileRepository {
    override suspend fun getProfile(): Result<ProfileModel> = withContext(Dispatchers.IO) {
        val response = userService.getProfile()
        if (response.isSuccessful) {
            if (response.body() != null) {
                val body = response.body()
                if (body != null) {
                    val profile = ProfileModel(
                        firstName = body.firstName,
                        lastName = body.lastName,
                        image = body.image,
                        email = body.email
                    )
                    return@withContext Result.Success(profile)
                }
            }
        }
        return@withContext Result.Error()
    }
}