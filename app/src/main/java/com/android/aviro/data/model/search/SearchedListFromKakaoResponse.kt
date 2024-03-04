package com.android.aviro.data.model.restaurant


import kotlinx.serialization.Serializable

// 카카오 검색 레스토랑 리스트 -> 필요한 정보만 mapping해서 ui에 뿌림
@Serializable
data class SearchedListFromKakaoResponse (
        val id : String,
        val meta : Meta,
        val total_count : Int,
        val documents : List<Document>,
)

data class Meta(
        val is_end : Boolean,
        val pageable_count : Int,
        val same_name : SameName,
)

data class SameName(
        val keyword : String,
        val region : List<String>,
        val selected_region : String,
)

data class Document(
        val id : String,
        val place_name : String,
        val distance : String,
        val x : String,
        val y : String,
        val category_name : String,
        val category_group_code : String,
        val category_group_name : String,
        val address_name : String,
        val road_address_name : String,
        val place_url : String,
        val phone : String
)

