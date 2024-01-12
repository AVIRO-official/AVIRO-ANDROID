package com.android.aviro.data.entity.marker

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.naver.maps.map.overlay.Marker


data class MarkerEntity (

    //@SerializedName("placeId")
    val placeId : String,
    //@SerializedName("veganType")
    val veganType : String,
    //@SerializedName("marker")
    //@Transient
    val marker : Marker

)


/*
class MarkerEntity(var placeId: String, var veganType: String, var marker: Marker) {

    fun getplaceId() : String {
        return placeId
    }
    fun getveganType() : String {
        return veganType
    }
    fun getmarker() : Marker {
        return marker
    }

}

 */
