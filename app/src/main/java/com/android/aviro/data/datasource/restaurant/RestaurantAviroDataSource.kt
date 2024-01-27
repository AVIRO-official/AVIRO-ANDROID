package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO

interface RestaurantAviroDataSource {

    suspend fun getRestaurant(request : RestaurantRequestDTO) : Result<DataBodyResponse<ReataurantReponseDTO>>
}