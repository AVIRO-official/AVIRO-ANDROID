package com.android.aviro.domain.entity.search

// 카카오에서 받은 정보 중 사용자가 화면에 표시하기 위해 필요한 정보
data class SearchedRestaurant(
    // meta 정보
    val is_end : Boolean,
    val total_count : Int,
    val pageable_count : Int,

    // 가게 정보
    val place_name : String, //가게명
    val distance : String,
    val x : String,
    val y : String,
    //val category_name : String,
    val category_group_code : String,
    //val category_group_name : String,
    val address_name : String,
    val road_address_name : String,
    val place_url : String,
    val phone : String
)
