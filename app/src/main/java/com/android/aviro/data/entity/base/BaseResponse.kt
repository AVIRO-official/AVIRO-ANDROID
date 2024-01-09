package com.android.aviro.data.entity.base

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ErrorResponse(
    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("message")
    val message: String
) : Parcelable