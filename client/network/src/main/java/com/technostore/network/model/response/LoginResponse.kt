package com.technostore.network.model.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class LoginResponse(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
)