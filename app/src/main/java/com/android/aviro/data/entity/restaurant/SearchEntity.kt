package com.android.aviro.data.entity.restaurant

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchEntity(

    @SerializedName("id")
    val id : String,
    @SerializedName("place_name")
    val place_name : String,
    @SerializedName("distance")
    val distance : String,
    @SerializedName("x")
    val x : String,
    @SerializedName("y")
    val y : String,

    @SerializedName("category_name")
    val category_name : String,
    @SerializedName("category_group_code")
    val category_group_code : String,
    @SerializedName("category_group_name")
    val category_group_name : String,

    @SerializedName("address_name")
    val address_name : String,
    @SerializedName("road_address_name")
    val road_address_name : String,

    @SerializedName("place_url")
    val place_url : String,
    @SerializedName("phone")
    val phone : String,

) : Parcelable
