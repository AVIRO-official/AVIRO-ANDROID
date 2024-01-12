package com.android.aviro.domain.repository

import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO

interface RestaurantRepository {

    suspend fun getRestaurant(request : RestaurantRequestDTO)
}