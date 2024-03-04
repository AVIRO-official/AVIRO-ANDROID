package com.android.aviro.data.model.marker

import com.naver.maps.map.overlay.Marker
import io.realm.kotlin.types.annotations.PrimaryKey

// 로컬 DB의 마커 DAO
data class MarkerDAO (
    @PrimaryKey
    val placeId : String,
    var veganType : String,
    val marker : Marker

)

