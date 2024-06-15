package com.aviro.android.domain.entity.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable



@Parcelize
//@Serializable
data class SearchedRestaurantItem(
   // @SerializedName("placeId")
    var placeId : String?,
    //@SerializedName("placeName")
    val placeName : String,
    //@SerializedName("placeAddress")
    val addressName : String,
    val roadAddressName : String, // val placeAddress : String,
    val phone : String?,
    //@SerializedName("distance")
    var distance : String,
    //@SerializedName("x")
    val x : String,
    //@SerializedName("y")
    val y : String,
    //@SerializedName("veganType")
    val veganType :VeganOptions
    ): Parcelable

@Parcelize
data class VeganOptions(
    var allVegan: Boolean,
    var someMenuVegan: Boolean,
    var ifRequestVegan: Boolean
): Parcelable

