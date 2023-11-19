package com.technostore.feature_login.business

import com.technostore.app_store.store.AppStore
import com.technostore.arch.result.ErrorMessage
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.sign_in.error.SignInError
import com.technostore.network.model.request.LoginRequest
import com.technostore.network.model.response.LoginResponse
import com.technostore.network.service.LoginService

const val ERROR_EMAIL = "The user with such email does not exist."
const val ERROR_PASSWORD = "Wrong password!"

class LoginRepositoryImpl(
    private val loginService: LoginService,
    private val appStore: AppStore
) :
    LoginRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> {

        val loginResponse = LoginResponse(
            email = email,
            password = password
        )
        val loginResult =
            loginService.signIn(loginResponse)
        if (loginResult.isSuccessful) {
            if (loginResult.body() != null) {
                val body = loginResult.body()!!
                appStore.refresh(
                    refreshToken = body.refreshToken,
                    expireTimeRefreshToken = body.expireTimeRefreshToken,
                    accessToken = body.accessToken,
                    expireTimeAccessToken = body.expireTimeAccessToken,
                    id = body.id,
                    email = body.email
                )
            }
            return Result.Success()
        } else {
            if (loginResult.code() == 404) {
                val errorMessage = ErrorMessage<LoginRequest>()
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
}