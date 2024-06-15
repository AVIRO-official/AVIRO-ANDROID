package com.aviro.android.domain.entity.search



// 카카오에서 받은 정보 중 사용자가 화면에 표시하기 위해 필요한 정보
data class SearchedRestaurantList(

    val is_end : Boolean,
    val searchedList: List<SearchedRestaurantItem>

)
