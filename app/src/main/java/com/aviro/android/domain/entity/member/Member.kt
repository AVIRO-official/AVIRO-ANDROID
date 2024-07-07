package com.aviro.android.domain.entity.member

data class Member(
    val refreshToken : String?,
    val accessToken : String?,
    val userId : String,
    val userName : String?,
    val userEmail : String?,
    val nickname : String,
    val birthday : Int?,
    val gender : String?,
    val marketingAgree : Boolean,
    val type : String,

    )
