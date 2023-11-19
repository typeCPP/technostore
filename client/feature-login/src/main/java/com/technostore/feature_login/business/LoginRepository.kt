package com.technostore.feature_login.business

import com.technostore.arch.result.Result

interface LoginRepository {

    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun checkEmailExists(email: String): Result<Boolean>
}