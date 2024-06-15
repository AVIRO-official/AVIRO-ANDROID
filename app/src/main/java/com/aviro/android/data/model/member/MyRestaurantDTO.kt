package com.aviro.android.data.model.member

data class MyRestaurantDTO(
    val placeId: String = "",
    val title: String = "",
    val category: String = "",
    val allVegan: Boolean = true,
    val someMenuVegan: Boolean = false,
    val ifRequestVegan: Boolean = false,
    val shortAddress: String = "",
    val menu: String = "",
    val menuCountExceptOne : Int = 0,
    val createdBefore : String = ""
)
