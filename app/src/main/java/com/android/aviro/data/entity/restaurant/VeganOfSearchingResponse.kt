package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName

data class VeganOfSearchingResponse (

        @SerializedName("statusCode")
        val statusCode : Int,
        @SerializedName("body")
        val body : List<VeganType>,

        )

data class VeganType(
        @SerializedName("index")
        val index : Int,
        @SerializedName("placeId")
        val placeId : String,
        @SerializedName("allVegan")
        val allVegan : Boolean,
        @SerializedName("someMenuVegan")
        val someMenuVegan : Boolean,
        @SerializedName("ifRequestVegan")
        val ifRequestVegan : Boolean
)