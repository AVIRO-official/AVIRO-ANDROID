package com.aviro.android.domain.entity.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RestaurantInfo(
    val placeId: String,
    val address: String,
    val address2: String?,
    val phone: String?,
    val url: String?,
    val haveTime: Boolean,
    val shopStatus: String?,
    val shopHours: String?,
    val updatedTime: String
) : Parcelable
