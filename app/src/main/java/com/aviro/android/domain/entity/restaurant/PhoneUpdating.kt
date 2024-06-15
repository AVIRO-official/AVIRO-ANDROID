package com.aviro.android.domain.entity.restaurant

data class PhoneUpdating(
    val placeId : String,
    val title : String,
    val userId : String,
    val nickname : String,
    val phone : BeforeAfterString
)

