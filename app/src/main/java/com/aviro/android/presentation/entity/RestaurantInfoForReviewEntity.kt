package com.aviro.android.presentation.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantInfoForReviewEntity(

    val placeId: String,
    val title: String,
    val category: String,
    val address: String,
    val veganTypeColor : String

):Parcelable
