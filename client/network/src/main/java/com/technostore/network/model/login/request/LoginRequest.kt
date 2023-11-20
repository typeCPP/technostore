package com.technostore.network.model.login.request

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)