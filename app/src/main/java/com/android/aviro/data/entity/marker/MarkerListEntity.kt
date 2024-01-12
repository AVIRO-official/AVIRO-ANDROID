package com.android.aviro.data.entity.marker

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class MarkerListEntity(
    @SerializedName("markerList")
    val markerList : List<MarkerEntity>
)