package com.aviro.android.data.model.restaurant

data class RestaurantReviewAddRequest(
    val commentId : String,
    val placeId : String,
    val userId : String,
    val content : String
)
