package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName

data class RestaurantInfo(

    @SerializedName("placeId")
    val placeId: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("address2")
    val address2: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("haveTime")
    val haveTime: Boolean,

    @SerializedName("shopStatus")
    val shopStatus: String,

    @SerializedName("shopHours")
    val shopHours: String,

    @SerializedName("updatedTime")
    val updatedTime: Int
)
