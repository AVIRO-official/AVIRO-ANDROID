package com.android.aviro.data.datasource.marker

import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.naver.maps.map.overlay.Marker

interface MarkerCacheDataSource {

    fun saveMarker(custom_marker : List<MarkerEntity>)
    fun getMarker() : MarkerListEntity
    fun updateMarker()

}