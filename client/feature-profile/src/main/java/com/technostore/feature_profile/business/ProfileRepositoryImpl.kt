package com.technostore.feature_profile.business

import com.technostore.app_store.store.AppStore
import com.technostore.arch.result.Result
import com.technostore.feature_profile.business.error.ChangePasswordError
import com.technostore.feature_profile.business.model.ProfileModel
import com.technostore.network.model.session.request.RefreshTokenRequest
import com.technostore.network.service.SessionService
import com.technostore.network.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
    private val userService: UserService,
    private val sessionService: SessionService,
    private val appStore: AppStore
) : ProfileRepository {
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

    override suspend fun changePassword(oldPassword: String, newPassword: String): Result<Unit> =
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
            if (response.code() == 404) {
                return@withContext Result.Error(ChangePasswordError.WrongOldPassword)
            }
            return@withContext Result.Error()
        }
}