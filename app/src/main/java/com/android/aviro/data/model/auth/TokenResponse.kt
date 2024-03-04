package com.android.aviro.data.model.auth


data class TokenResponse(
    val isMember: Boolean,
    val refreshToken: String,
    val accessToken: String,
    val userId: String
)

