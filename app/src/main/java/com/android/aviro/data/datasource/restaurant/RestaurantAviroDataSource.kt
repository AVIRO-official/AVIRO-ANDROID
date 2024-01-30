package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO
import com.android.aviro.data.entity.restaurant.SearchRestaurantResponse

interface RestaurantAviroDataSource {

    suspend fun getRestaurant(request : RestaurantRequestDTO) : Result<DataBodyResponse<ReataurantReponseDTO>>
    suspend fun getSearchedRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : Result<SearchRestaurantResponse>
}