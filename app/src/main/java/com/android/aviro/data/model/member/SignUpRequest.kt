package com.android.aviro.data.model.member

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class SignUpRequest(

    val refreshToken : String,
    val accessToken : String,
    val userId : String,
    val userName : String?,
    val userEmail : String,
    val nickname : String,
    val birthday : Int?,
    val gender : String?,
    val marketingAgree : Boolean,

    )