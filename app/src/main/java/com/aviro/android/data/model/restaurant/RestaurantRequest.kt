package com.aviro.android.data.model.restaurant

import com.aviro.android.data.model.menu.MenuDTO

data class RestaurantRequest (
    val placeId: String,
    val userId: String,
    val title: String,
    val category: String,
    val address: String,
    val phone: String?,
    val x: Double?,
    val y: Double?,
    val allVegan : Boolean,
    val someMenuVegan : Boolean,
    val ifRequestVegan : Boolean,
    val menuArray : List<MenuDTO>
    )