package com.android.aviro.data.datasource.marker

import com.naver.maps.map.overlay.Marker

interface MarkerLocalDataSource {

    fun saveMarker(placeId : String, marker : Marker)
    fun getMarker()
    fun updateMarker()

}