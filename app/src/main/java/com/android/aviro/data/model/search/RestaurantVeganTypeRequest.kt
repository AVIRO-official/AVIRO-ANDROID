package com.android.aviro.data.model.restaurant


data class RestaurantVeganTypeRequest (
    val RequestedRestaurantVeganTypeList : List<RequestedRestaurant>
)

data class RequestedRestaurant(
    val title : String,
    val x : Double,
    val y : Double
)
