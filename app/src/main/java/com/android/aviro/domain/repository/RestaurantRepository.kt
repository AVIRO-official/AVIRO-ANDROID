package com.android.aviro.domain.repository

import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.restaurant.*
import com.android.aviro.domain.entity.SearchedRestaurantItem

interface RestaurantRepository {

    suspend fun getRestaurantLoc(x : String, y : String, wide : String, time : String, isInitMap : Boolean) : MappingResult
    fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantReponseDTO) : List<MarkerEntity>?
    suspend fun getRestaurantDetail()
    suspend fun searchRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : Result<SearchedPlaceListResponse>
    suspend fun getVeganTypeOfSearching(request : List<Document>) : Result<List<SearchedRestaurantItem>>

}