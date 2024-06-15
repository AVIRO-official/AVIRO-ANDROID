package com.aviro.android.data.model.restaurant


data class RestaurantSummaryResponse (
    val placeId: String,
    val title: String,
    val category: String,
    val address: String,
    val commentCount: Int,
    val bookmark : Boolean
)
