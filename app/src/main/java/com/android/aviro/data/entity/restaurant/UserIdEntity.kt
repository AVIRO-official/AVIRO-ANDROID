package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer

data class UserIdEntity(
    @SerializedName("userId")
    val userId: String
)
