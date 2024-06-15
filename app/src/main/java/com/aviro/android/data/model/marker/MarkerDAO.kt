package com.aviro.android.data.model.marker

import com.naver.maps.map.overlay.Marker
import io.realm.kotlin.types.annotations.PrimaryKey

// 로컬 DB의 마커 DAO
data class MarkerDAO (
    @PrimaryKey
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

