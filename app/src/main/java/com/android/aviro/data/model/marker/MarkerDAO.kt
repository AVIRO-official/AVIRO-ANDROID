package com.android.aviro.data.model.marker

import com.naver.maps.map.overlay.Marker


data class MarkerEntity (

    //@SerializedName("placeId")
    val placeId : String,
    //@SerializedName("veganType")
    var veganType : String,
    //@SerializedName("marker")
    val marker : Marker

)

