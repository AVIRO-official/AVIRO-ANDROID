package com.aviro.android.data.model.kakao.coordi



data class CoordOfAddressFromKakaoResponse(
    val meta : Meta,
    val documents : List<Document>,
)


data class Meta(
    val total_count : Int,
    val pageable_count : Int,
    val is_end : Boolean,

    )

data class Document(
    val address_name : String,
    val address_type : String,
    val x : String,
    val y : String,
    val address : Address?,
    val road_address : RoadAddress?,
    )


data class Address(
    val address_name : String,
    val region_1depth_name : String,
    val region_2depth_name : String,
    val region_3depth_name : String,
    val h_code : String,
    val b_code : String,
    val mountain_yn : String,
    val main_address_no : String,
    val sub_address_no : String,
    val x : String,
    val y : String
)

data class RoadAddress(
    val address_name : String,
    val region_1depth_name : String,
    val region_2depth_name : String,
    val region_3depth_name : String,
    val road_name : String,
    val underground_yn : String,
    val main_building_no : String,
    val sub_building_no : String,
    val building_name : String,
    val zone_no : String,
    val x : String,
    val y : String
)
