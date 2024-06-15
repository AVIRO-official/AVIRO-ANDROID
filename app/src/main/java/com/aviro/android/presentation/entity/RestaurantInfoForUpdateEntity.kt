package com.aviro.android.presentation.entity

import android.os.Parcelable
import com.aviro.android.domain.entity.menu.Menu
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantInfoForUpdateEntity(
    val placeId: String,

    val title: String,
    val category: String,

    val address: String,
    val address2: String?,

    val phone: String?,
    val url: String?,

    val x: Double,
    val y: Double,

    val allVegan : Boolean,
    val someMenuVegan : Boolean,
    val ifRequestVegan : Boolean,

    val menuArray : List<Menu>

) : Parcelable
