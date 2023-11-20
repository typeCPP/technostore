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
import com.technostore.network.model.session.request.RefreshTokenRequest
import com.technostore.network.service.LoginService
import com.technostore.network.service.SessionService
import com.technostore.network.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginRepositoryImpl(
    private val loginService: LoginService,
    private val appStore: AppStore,
    private val sessionService: SessionService,
    private val userService: UserService
) : LoginRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val loginRequest = LoginRequest(
                email = email,
                password = password
            )
            val loginResult = loginService.signIn(loginRequest)
            if (loginResult.isSuccessful) {
                if (loginResult.body() != null) {
                    val body = loginResult.body()!!
                    refreshData(body)
                    appStore.isActive = true
                }
                return@withContext Result.Success()
            } else {
                if (loginResult.code() == 404) {
                    val errorMessage = ErrorMessage<LoginResponse>()
                    val message = errorMessage.getErrorMessage(loginResult)
                    if (message == ERROR_PASSWORD) {
                        return@withContext Result.Error(SignInError.ErrorPassword)
                    } else if (message == ERROR_EMAIL) {
                        return@withContext Result.Error(SignInError.ErrorEmail)
                    }
                }
                return@withContext Result.Error()
            }
        }

    override suspend fun checkEmailExists(email: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val response = loginService.checkEmailExists(email)
            if (response.isSuccessful) {
                val body = response.body()?.exists
                if (body != null) {
                    return@withContext Result.Success(body)
                }
            }
            return@withContext Result.Error(ErrorType())
        }

    override suspend fun signUp(
        name: String,
        lastName: String,
        email: String,
        password: String,
        byteArray: ByteArray?
    ): Result<Unit> = withContext(Dispatchers.IO) {
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
            return@withContext Result.Success()
        }
        return@withContext Result.Error()
    }

    override suspend fun checkRecoveryCodeForAccountConfirmations(
        code: String,
        email: String
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        val response = loginService.checkRecoveryCodeForAccountConfirmations(code, email)
        if (response.isSuccessful) {
            appStore.isActive = true
            return@withContext Result.Success(true)
        }
        if (response.code() == 404) {
            return@withContext Result.Success(false)
        }
        return@withContext Result.Error(ErrorType())
    }

    override suspend fun changePassword(newPassword: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            if (!appStore.refreshTokenIsValid()) {
                val sessionResponse = sessionService.refreshTokens(
                    RefreshTokenRequest(
                        generateRefreshToken = true,
                        email = appStore.email.orEmpty(),
                        accessToken = appStore.accessToken.orEmpty(),
                        refreshToken = appStore.refreshToken.orEmpty()
                    )
                ).execute().body()
                sessionResponse?.let {
                    appStore.refreshAccessToken(
                        sessionResponse.accessToken,
                        it.expireTimeAccessToken
                    )
                    sessionResponse.refreshToken?.let { refreshToken ->
                        sessionResponse.expireTimeRefreshToken?.let { accessToken ->
                            appStore.refreshRefreshToken(
                                refreshToken,
                                accessToken
                            )
                        }
                    }
                }
            }
            val response = userService.changePassword(
                refreshToken = appStore.refreshToken.orEmpty(),
                oldPassword = null,
                newPassword = newPassword
            )
            if (response.isSuccessful) {
                return@withContext Result.Success()
            }
            return@withContext Result.Error()
        }

    override suspend fun sendCodeForRecoveryPassword(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val response = loginService.sendCodeForRecoveryPassword(email)
            if (response.isSuccessful) {
                return@withContext Result.Success()
            }
            return@withContext Result.Error()
        }

    override suspend fun checkRecoveryCode(code: String, email: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val response = loginService.checkRecoveryCode(code, email)
            if (response.isSuccessful) {
                if (response.body() != null) {
                    val body = response.body()!!
                    appStore.refresh(
                        refreshToken = body.refreshToken,
                        expireTimeRefreshToken = body.expireTimeRefreshToken,
                        accessToken = body.accessToken,
                        expireTimeAccessToken = body.expireTimeAccessToken,
                        id = body.id,
                        email = body.email
                    )
                }
                return@withContext Result.Success(true)
            } else {
                if (response.code() == 409) {
                    return@withContext Result.Success(false)
                }
            }
            return@withContext Result.Error()
        }

    override suspend fun sendCodeForAccountConfirmations(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val id = appStore.id
            if (!id.isNullOrEmpty()) {
                val response = loginService.sendCodeForAccountConfirmations(id.toLong())
                if (response.isSuccessful) {
                    return@withContext Result.Success()
                }
            }
            return@withContext Result.Error()
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