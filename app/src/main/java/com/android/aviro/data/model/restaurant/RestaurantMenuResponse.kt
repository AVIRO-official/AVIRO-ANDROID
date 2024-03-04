package com.android.aviro.data.model.restaurant

import com.android.aviro.data.model.menu.MenuDTO

data class RestaurantMenuResponse(
    val count: Int,
    val updatedTime: Int,
    val menuArray: List<MenuDTO>
)


