package com.android.aviro.data.model.auth

data class SignInResponse(
    val userId : String,
    val userName : String?,
    val userEmail : String,
    val nickname : String,
    val marketingAgree : Int,
    )