package com.technostore.network.model.login.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class LoginResponse(
    @SerializedName("expireTimeRefreshToken")
    val expireTimeRefreshToken: String,
    @SerializedName("refresh-token")
    val refreshToken: String,
    @SerializedName("expireTimeAccessToken")
    val expireTimeAccessToken: String,
    @SerializedName("access-token")
    val accessToken: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String
)