package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO

interface RestaurantDataSource {

    suspend fun getRestaurant(request : RestaurantRequestDTO) : Result<ReataurantReponseDTO>
}