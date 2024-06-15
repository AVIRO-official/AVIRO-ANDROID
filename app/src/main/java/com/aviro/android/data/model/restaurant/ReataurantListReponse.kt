package com.aviro.android.data.model.restaurant

// 어비로 서버 레스토랑 리스트 -> 마커로 변활될 예정
data class ReataurantListReponse(
    val amount: Int,
    val deletedPlace: List<String>?,
    val updatedPlace: List<RestaurantDAO>?,
    )
