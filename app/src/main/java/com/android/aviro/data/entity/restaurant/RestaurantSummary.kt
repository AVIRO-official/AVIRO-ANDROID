package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName

data class RestaurantSummary(
    @SerializedName("placeId")
    val placeId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("commentCount")
    val commentCount: Int
)
