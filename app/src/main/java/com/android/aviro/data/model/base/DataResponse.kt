package com.android.aviro.data.model.base

import com.google.gson.annotations.SerializedName

data class DataBodyResponse<T>(

    @SerializedName("statusCode")
    val statusCode : Int,

    @SerializedName("message")
    val message : String?,

    @SerializedName("data")
    val data: T?

)