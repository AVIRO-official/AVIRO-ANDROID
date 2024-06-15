package com.aviro.android.data.model.restaurant

data class RestaurantReportRequest(
    val placeId : String,
    val userId : String,
    val nickname : String,
    val code : Int
)
