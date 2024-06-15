package com.aviro.android.data.model.member


data class SignUpRequest(

    val refreshToken : String?,
    val accessToken : String?,
    val userId : String,
    val userName : String?,
    val userEmail : String,
    val nickname : String,
    val birthday : Int?,
    val gender : String?,
    val marketingAgree : Boolean,
    val type : String,

    )