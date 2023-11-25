package com.technostore.network.model.session.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class RefreshTokenResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("refresh-token")
    val refreshToken: String?,
    @SerializedName("expireTimeRefreshToken")
    val expireTimeRefreshToken: String?,
    @SerializedName("expireTimeAccessToken")
    val expireTimeAccessToken: String,
    @SerializedName("access-token")
    val accessToken: String,
)