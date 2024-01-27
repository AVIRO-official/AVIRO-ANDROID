package com.android.aviro.data.entity.marker

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.naver.maps.map.overlay.Marker


data class MarkerEntity (

    //@SerializedName("placeId")
    val placeId : String,
    //@SerializedName("veganType")
    var veganType : String,
    //@SerializedName("marker")
    val marker : Marker

)

