package com.technostore.feature_login.business

import com.technostore.app_store.store.AppStore
import com.technostore.arch.result.ErrorMessage
import com.technostore.arch.result.ErrorType
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.sign_in.error.Message.ERROR_EMAIL
import com.technostore.feature_login.business.sign_in.error.Message.ERROR_PASSWORD
import com.technostore.feature_login.business.sign_in.error.SignInError
import com.technostore.network.model.login.request.LoginRequest
import com.technostore.network.model.login.response.LoginResponse
import com.technostore.network.service.LoginService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginRepositoryImpl(
    private val loginService: LoginService,
    private val appStore: AppStore
) : LoginRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        val loginRequest = LoginRequest(
            email = email,
            password = password
        )
        val loginResult = loginService.signIn(loginRequest)
        if (loginResult.isSuccessful) {
            if (loginResult.body() != null) {
                val body = loginResult.body()!!
                refreshData(body)
            }
            return Result.Success()
        } else {
            if (loginResult.code() == 404) {
                val errorMessage = ErrorMessage<LoginResponse>()
                val message = errorMessage.getErrorMessage(loginResult)
                if (message == ERROR_PASSWORD) {
                    return Result.Error(SignInError.ErrorPassword)
                } else if (message == ERROR_EMAIL) {
                    return Result.Error(SignInError.ErrorEmail)
                }
            }
            return Result.Error()
        }
    }

    override suspend fun checkEmailExists(email: String): Result<Boolean> {
        val response = loginService.checkEmailExists(email)
        if (response.isSuccessful) {
            val body = response.body()?.exists
            if (body != null) {
                return Result.Success(body)
            }
        }
        return Result.Error(ErrorType())
    }

    override suspend fun signUp(
        name: String,
        lastName: String,
        email: String,
        password: String,
        byteArray: ByteArray?
    ): Result<Unit> {
        val data = JSONObject(
            mapOf(
                "name" to name,
                "lastName" to lastName,
                "email" to email,
                "password" to password
            )
        ).toString()

        val dataMultipart = data.toRequestBody("text/plain".toMediaTypeOrNull())
        var part: MultipartBody.Part? = null
        if (byteArray != null) {
            val fileMultipart = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                byteArray
            )
            part = MultipartBody.Part.createFormData("file", "data", fileMultipart)
        }
        val response = loginService.signUp(dataMultipart, part)
        if (response.isSuccessful) {
            val body = response.body()!!
            refreshData(body)
            return Result.Success()
        }
        return Result.Error()
    }


    private fun refreshData(body: LoginResponse) {
        appStore.refresh(
            refreshToken = body.refreshToken,
            expireTimeRefreshToken = body.expireTimeRefreshToken,
            accessToken = body.accessToken,
            expireTimeAccessToken = body.expireTimeAccessToken,
            id = body.id,
            email = body.email
        )
    }
}