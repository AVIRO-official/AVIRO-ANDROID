package com.android.aviro.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class MemberEntity(

    @SerializedName("refreshToken")
    val refreshToken : String,
    @SerializedName("accessToken")
    val accessToken : String,

    @SerializedName("userId")
    val userId : Int,
    @SerializedName("userName")
    val userName : String,
    @SerializedName("userEmail")
    val userEmail : String,
    @SerializedName("nickname")
    val nickname : String,
    @SerializedName("birthday")
    val birthday : Int?,
    @SerializedName("gender")
    val gender : String?,
    @SerializedName("marketingAgree")
    val marketingAgree : Boolean,

    ) : Parcelable