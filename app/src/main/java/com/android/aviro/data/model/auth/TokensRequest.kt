package com.android.aviro.data.model.auth

data class TokensRequest(
    val identityToken : String,
    val authorizationCode : String

)

