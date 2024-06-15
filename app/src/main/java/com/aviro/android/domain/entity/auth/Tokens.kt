package com.aviro.android.domain.entity.auth

data class Tokens(
    val isMember: Boolean,
    val refreshToken: String,
    val accessToken: String,
    val userId: String
)
