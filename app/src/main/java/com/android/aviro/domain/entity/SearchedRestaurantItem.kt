package com.android.aviro.domain.entity


data class SearchedRestaurantItem(

    var placeId : String?,

    val placeName : String,

    val placeAddress : String,

    val distance : String,

    val x : String,

    val y : String,

    val veganType :VeganOptions

    )

data class VeganOptions(
    var allVegan: Boolean,
    var someMenuVegan: Boolean,
    var ifRequestVegan: Boolean
)

