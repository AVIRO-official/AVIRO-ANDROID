package com.android.aviro.domain.entity.restaurant

data class RestaurantSummary (
    val placeId: String,
    val title: String,
    val category: String,
    val address: String,
    val commentCount: Int
        )