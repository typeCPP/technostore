package com.technostore.network.model.user.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class UserProfileResponse(
    @SerializedName("name")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("email")
    val email: String,
)