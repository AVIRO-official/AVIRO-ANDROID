package com.android.aviro.data.entity.base

import com.google.gson.annotations.SerializedName

sealed class MappingResult  {
    data class Success<T>(
        @SerializedName("statusCode")
        val statusCode : Int,

        @SerializedName("message")
        val message : String?,

        @SerializedName("data")
        val data: T?
    ) : MappingResult()

    data class Error(val code: Int, val message: String?) : MappingResult()
}
