package com.android.aviro.domain.entity.restaurant

import com.android.aviro.data.model.menu.MenuDTO
import com.android.aviro.domain.entity.menu.Menu

data class RestaurantMenu(
    val count: Int,
    val updatedTime: Int,
    val menuArray: List<Menu>
)
