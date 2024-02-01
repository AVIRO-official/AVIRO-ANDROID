package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName

data class VeganOfSearchingRequest(
    @SerializedName("placeArray")
    val placeArray : List<PlaceInfo>

)

data class PlaceInfo(
    @SerializedName("title")
    val title : String,
    @SerializedName("x")
    val x : Double,
    @SerializedName("y")
    val y : Double
)
