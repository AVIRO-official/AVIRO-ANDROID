package com.android.aviro.data.entity.restaurant

import com.google.gson.annotations.SerializedName

data class BookmarkResponse(

    @SerializedName("statusCode")
    val statusCode: Int,

    @SerializedName("bookmarks")
    val bookmarks: List<String>

)
