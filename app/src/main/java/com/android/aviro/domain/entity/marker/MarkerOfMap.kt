package com.android.aviro.domain.entity.marker

import com.naver.maps.map.overlay.Marker

data class MarkerOfMap(
    val placeId : String,
    var veganType : String,
    val marker : Marker
)
