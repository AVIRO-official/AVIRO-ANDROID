package com.android.aviro.data.model.restaurant

import com.android.aviro.domain.entity.MenuEntity
import com.google.gson.annotations.SerializedName

data class RestaurantMenu(

    @SerializedName("count")
    val count: Int,

    @SerializedName("updatedTime")
    val updatedTime: Int,

    @SerializedName("menuArray")
    val menuArray: List<MenuEntity>


)
