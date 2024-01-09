package com.android.aviro.data.entity.member

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NicknameCheckResponse(

    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("isValid")
    val isValid: Boolean?,

    @SerializedName("message")
    val message: String,

) : Parcelable
