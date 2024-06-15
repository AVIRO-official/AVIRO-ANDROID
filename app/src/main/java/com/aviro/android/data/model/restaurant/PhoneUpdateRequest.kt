package com.aviro.android.data.model.restaurant

data class PhoneUpdateRequest(
    val placeId : String,
    val title : String,
    val userId : String,
    val nickname : String,
    val phone : ChangedString
)

