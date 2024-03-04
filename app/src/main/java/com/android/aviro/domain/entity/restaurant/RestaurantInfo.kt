package com.android.aviro.domain.entity.restaurant

data class RestaurantInfo(
    val placeId: String,
    val address: String,
    val address2: String,
    val phone: String,
    val url: String,
    val haveTime: Boolean,
    val shopStatus: String,
    val shopHours: String,
    val updatedTime: Int
)
