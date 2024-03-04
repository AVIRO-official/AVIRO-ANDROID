package com.android.aviro.domain.repository

import com.android.aviro.data.model.marker.MarkerDAO
import com.android.aviro.data.model.restaurant.*
import com.android.aviro.domain.entity.SearchedRestaurantItem
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.marker.MarkerOfMap

interface RestaurantRepository {

    suspend fun getRestaurantLoc(isInitMap : Boolean, x : String, y : String, wide : String, time : String) : MappingResult
    fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantListReponse) : List<MarkerDAO>?
    //fun getMarkerForBookmark() : List<MarkerDAO>? //bookmark_list : List<String>
    suspend fun getRestaurantSummary(placeId : String) : MappingResult
    suspend fun searchRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : MappingResult //Result<SearchedPlaceListResponse>
    suspend fun getVeganTypeOfSearching(isEnd : Boolean, SearchedPlaceRawList : List<Document>) : MappingResult
    suspend fun getBookmarkRestaurant(userId : String) : MappingResult


}