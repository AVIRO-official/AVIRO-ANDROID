package com.android.aviro.data.model.member

data class NicknameCheckResponse(
    val statusCode: Int,
    val isValid: Boolean?,
    val message: String,
)
