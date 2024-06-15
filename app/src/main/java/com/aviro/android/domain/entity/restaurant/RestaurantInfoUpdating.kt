package com.aviro.android.domain.entity.restaurant

data class RestaurantInfoUpdating(
    val placeId : String,
    val title : String,
    val changedTitle : BeforeAfterString?,
    val category: BeforeAfterString?,
    val address: BeforeAfterString?,
    val address2: BeforeAfterString?,
    val x : BeforeAfterDouble?,
    val y : BeforeAfterDouble?
)
// val userId : String,
//    val nickname : String,
