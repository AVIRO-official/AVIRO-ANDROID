package com.aviro.android.domain.entity.restaurant

data class UrlUpdating(
    val placeId : String,
    val title : String,
    val userId : String,
    val nickname : String,
    val url : BeforeAfterString
)
