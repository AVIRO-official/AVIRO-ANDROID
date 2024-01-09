package com.android.aviro.data.entity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokensResponseDTO(

    @SerializedName("isMember")
    val isMember: Boolean,

    @SerializedName("refreshToken")
    val refreshToken: String,

    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("userId")
    val userId: String

) : Parcelable

