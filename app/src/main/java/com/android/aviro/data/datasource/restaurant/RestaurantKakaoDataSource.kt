package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.model.restaurant.SearchedListFromKakaoResponse

interface RestaurantKakaoDataSource {
    suspend fun getSearchedRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : Result<SearchedListFromKakaoResponse>

}