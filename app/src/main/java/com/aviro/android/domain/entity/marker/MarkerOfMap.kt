package com.aviro.android.domain.entity.marker

import com.naver.maps.map.overlay.Marker


data class MarkerOfMap(
    val placeId : String,
    var title : String,
    var category : String,
    var veganTypeColor : String,
    var allVegan: Boolean,
    var someMenuVegan: Boolean,
    var ifRequestVegan: Boolean,
    val x : Double,
    val y : Double,
    val marker : Marker
)
