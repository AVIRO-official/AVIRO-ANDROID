package com.aviro.android.domain.entity.search

data class SearchedRestaurant(
    // 가게 정보
    val place_name : String, //가게명
    val distance : String,
    val x : String,
    val y : String,
    val address_name : String,
    val road_address_name : String,
    val place_url : String,
    val phone : String

)
