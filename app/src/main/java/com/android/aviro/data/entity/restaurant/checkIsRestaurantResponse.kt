package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName

data class checkIsRestaurantResponse(

    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("registered")
    val registered: Boolean
)

