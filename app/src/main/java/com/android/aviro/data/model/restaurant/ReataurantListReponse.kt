package com.android.aviro.data.model.restaurant


data class ReataurantReponseDTO(
    val amount: Int,
    val deletedPlace: List<String>?,
    val updatedPlace: List<RestaurantDAO>?,
    )
