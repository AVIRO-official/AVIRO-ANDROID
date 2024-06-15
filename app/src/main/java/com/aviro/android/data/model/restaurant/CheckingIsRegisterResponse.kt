package com.aviro.android.data.model.restaurant

// 따로 mapping 하지 않고 bool 값만 전달
data class CheckingIsRegisterResponse(
    val statusCode: Int,
    val registered: Boolean
)

