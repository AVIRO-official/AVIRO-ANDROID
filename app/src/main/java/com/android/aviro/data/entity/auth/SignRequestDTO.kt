package com.android.aviro.data.entity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class
SignRequestDTO(

    @SerializedName("refreshToken")
    val refreshToken : String,

) : Parcelable
