package com.aviro.android.data.model.restaurant

import com.aviro.android.data.model.menu.MenuDTO

data class RestaurantMenuResponse(
    val count: Int,
    val updatedTime: String,
    val menuArray: List<MenuDTO>
)


