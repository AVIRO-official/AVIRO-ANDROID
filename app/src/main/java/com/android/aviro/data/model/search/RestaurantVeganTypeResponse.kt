package com.android.aviro.data.model.restaurant

data class RestaurantVeganTypeResponse (
        val placeList : List<VeganType>
        )

data class VeganType(
        val index : Int?,
        val placeId : String,
        val allVegan : Boolean,
        val someMenuVegan : Boolean,
        val ifRequestVegan : Boolean

)

