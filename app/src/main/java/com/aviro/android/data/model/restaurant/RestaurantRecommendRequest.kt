package com.aviro.android.data.model.restaurant

data class RestaurantRecommendRequest(
    val placeId: String,
    val userId: String,
    val recommend: Boolean
)
