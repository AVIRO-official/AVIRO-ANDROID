package com.android.aviro.data.datasource.marker

import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.android.aviro.data.entity.restaurant.LocOfRestaurant
import com.naver.maps.map.overlay.Marker

interface MarkerMemoryCacheDataSource {

    fun getSize() : Int
    fun getAllMarker() : List<MarkerEntity>?
    fun getMarker(key_list : List<String>) : List<MarkerEntity>
    fun createMarker(updated_marker : LocOfRestaurant) : MarkerEntity
    fun updateMarker(marker_id : List<LocOfRestaurant>) : List<MarkerEntity>
    fun deleteMarker(marker_id : List<String>)

}