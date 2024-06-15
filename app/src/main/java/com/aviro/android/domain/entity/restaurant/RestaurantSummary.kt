package com.aviro.android.domain.entity.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantSummary (
    val placeId: String,
    val title: String,
    val category: String,
    val address: String,
    val commentCount: Int,
    val bookmark: Boolean
) : Parcelable