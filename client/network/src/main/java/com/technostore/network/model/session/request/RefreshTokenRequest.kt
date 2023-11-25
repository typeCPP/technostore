package com.technostore.network.model.session.request

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class RefreshTokenRequest(
    @SerializedName("generateRefreshToken")
    val generateRefreshToken: Boolean,
    @SerializedName("email")
    val email: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("accessToken")
    val accessToken: String
)