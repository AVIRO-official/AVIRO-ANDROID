package com.aviro.android.domain.entity.restaurant

import com.aviro.android.domain.entity.menu.Menu


data class Restaurant(
    val placeId: String,
    val userId: String,
    val title: String,
    val category: String,
    val address: String,
    val phone: String?,
    val x: Double,
    val y: Double,
    val allVegan : Boolean,
    val someMenuVegan : Boolean,
    val ifRequestVegan : Boolean,
    val menuArray : List<Menu>
)
