package com.technostore.feature_login.business

import com.technostore.arch.result.Result

interface LoginRepository {

    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun checkEmailExists(email: String): Result<Boolean>
    suspend fun signUp(
        name: String,
        lastName: String,
        email: String,
        password: String,
        byteArray: ByteArray?
    ): Result<Unit>

    suspend fun checkRecoveryCodeForAccountConfirmations(
        code: String, email: String
    ): Result<Boolean>
}