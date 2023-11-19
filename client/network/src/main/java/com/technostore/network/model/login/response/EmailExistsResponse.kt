package com.technostore.network.model.login.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class EmailExistsResponse(
    @SerializedName("exists") val exists: Boolean
)