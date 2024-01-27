package com.android.aviro.data.entity.restaurant

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//@Parcelize
data class ReataurantReponseDTO(
    @SerializedName("amount")
    val amount: Int,

    @SerializedName("deletedPlace")
    val deletedPlace: List<String>?,

    @SerializedName("updatedPlace")
    val updatedPlace: List<LocOfRestaurant>?,

    )
