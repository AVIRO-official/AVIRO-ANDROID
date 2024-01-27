package com.android.aviro.domain.repository

import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO

interface RestaurantRepository {

    suspend fun getRestaurantLoc(x : String, y : String, wide : String, time : String, isInitMap : Boolean) : MappingResult
    fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantReponseDTO) : List<MarkerEntity>?
    suspend fun getRestaurantDetail()
    suspend fun searchRestaurant()


}