package com.aviro.android.domain.entity.search

data class RestaurantVeganType(
    val RestaurantVeganTypeList : List<VeganType>
)
data class VeganType(
    val index : Int?,
    val placeId : String,
    val allVegan : Boolean,
    val someMenuVegan : Boolean,
    val ifRequestVegan : Boolean

)
