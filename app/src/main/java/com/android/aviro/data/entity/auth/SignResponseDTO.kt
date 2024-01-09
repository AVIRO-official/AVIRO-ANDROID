package com.android.aviro.data.entity.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class SignResponseDTO(

    @SerializedName("userId")
    val userId : Int,
    @SerializedName("userName")
    val userName : String,
    @SerializedName("userEmail")
    val userEmail : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("marketingAgree")
    val marketingAgree : Int,

    ) : Parcelable