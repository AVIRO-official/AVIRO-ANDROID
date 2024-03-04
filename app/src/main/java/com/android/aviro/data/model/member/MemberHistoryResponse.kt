package com.android.aviro.data.model.member

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class MyInfoCountResponse(

    @SerializedName("placeCount")
    val placeCount: Int,

    @SerializedName("commentCount")
    val commentCount: Int,

    @SerializedName("bookmarkCount")
    val bookmarkCount: Int,

    ) : Parcelable

