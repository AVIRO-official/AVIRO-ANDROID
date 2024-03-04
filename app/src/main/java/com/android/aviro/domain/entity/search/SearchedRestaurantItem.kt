package com.android.aviro.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


// SearchedRestaurant 와 RestaurantVeganTypeList 를 조합
@Parcelize
@Serializable
data class SearchedRestaurantItem(
    @SerializedName("placeId")
    var placeId : String?,
    @SerializedName("placeName")
    val placeName : String,
    @SerializedName("placeAddress")
    val placeAddress : String,
    @SerializedName("distance")
    val distance : String,
    @SerializedName("x")
    val x : String,
    @SerializedName("y")
    val y : String,
    @SerializedName("veganType")
    val veganType :VeganOptions
    ): Parcelable

@Parcelize
data class VeganOptions(
    var allVegan: Boolean,
    var someMenuVegan: Boolean,
    var ifRequestVegan: Boolean
): Parcelable

