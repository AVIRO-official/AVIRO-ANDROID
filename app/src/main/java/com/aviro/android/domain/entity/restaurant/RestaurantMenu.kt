package com.aviro.android.domain.entity.restaurant

import com.aviro.android.domain.entity.menu.Menu

data class RestaurantMenu(
    val count: Int,
    val updatedTime: String,
    val menuArray: List<Menu>
)
