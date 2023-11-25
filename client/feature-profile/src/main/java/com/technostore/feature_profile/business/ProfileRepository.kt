package com.technostore.feature_profile.business

import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.model.ProfileModel

interface ProfileRepository {

   suspend fun getProfile(): Result<ProfileModel>
}