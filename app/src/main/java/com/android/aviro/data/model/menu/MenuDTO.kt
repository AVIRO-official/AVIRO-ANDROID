package com.android.aviro.data.model.restaurant

import com.google.gson.annotations.SerializedName
import com.naver.maps.map.overlay.Marker

data class MenuEntity(

    @SerializedName("menuId")
    val menuId : String,

    @SerializedName("menuType")
    var menuType : String,

    @SerializedName("menu")
    var menu : String,

    @SerializedName("price")
    var price : String,

    @SerializedName("howToRequest")
    var howToRequest : String?,

    @SerializedName("isCheck")
    val isCheck : Boolean

)
