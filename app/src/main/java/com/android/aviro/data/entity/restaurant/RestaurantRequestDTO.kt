package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName

data class RestaurantRequestDTO (
    @SerializedName("x")
    val x: String,

    @SerializedName("y")
    val y: String,

    @SerializedName("wide")
    val wide: String,

    @SerializedName("time")
    val time: String,
        )