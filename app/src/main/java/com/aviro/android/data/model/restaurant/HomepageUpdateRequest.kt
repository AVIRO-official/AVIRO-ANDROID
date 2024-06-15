package com.aviro.android.data.model.restaurant

data class HomepageUpdateRequest(
    val placeId : String,
    val userId : String,
    val nickname : String,
    val title : String,
    val url : ChangedString
)

