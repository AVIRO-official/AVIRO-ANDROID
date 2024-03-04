package com.android.aviro.data.model.restaurant


data class RestaurantInfoResponse(

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
