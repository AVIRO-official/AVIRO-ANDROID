package com.aviro.android.data.model.auth

data class TokensRequest(
    val identityToken : String,
    val authorizationCode : String

)

