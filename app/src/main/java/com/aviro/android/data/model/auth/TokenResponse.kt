package com.aviro.android.data.model.auth


data class TokenResponse(
    val isMember: Boolean,
    val refreshToken: String,
    val accessToken: String,
    val userId: String
)

