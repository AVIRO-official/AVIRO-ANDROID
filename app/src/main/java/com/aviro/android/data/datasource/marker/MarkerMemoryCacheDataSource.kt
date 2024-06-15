package com.aviro.android.data.datasource.marker

import com.aviro.android.data.model.marker.MarkerDAO
import com.aviro.android.data.model.restaurant.RestaurantDAO

interface MarkerMemoryCacheDataSource {

    fun getSize() : Int
    fun getAllMarker() : List<MarkerDAO>?
    fun getMarker(key_list : List<String>) : List<MarkerDAO>
    fun createMarker(updated_marker : RestaurantDAO) : MarkerDAO
    fun updateMarker(marker_id : List<RestaurantDAO>) : List<MarkerDAO>
    fun deleteMarker(marker_id : List<String>)

}