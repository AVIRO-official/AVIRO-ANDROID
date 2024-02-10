package com.android.aviro.domain.entity

import com.google.gson.annotations.SerializedName

data class ReviewEntity(

    @SerializedName("commentId")
    val commentId : String,

    @SerializedName("userId")
    var userId : String,

    @SerializedName("content")
    var content : String,

    @SerializedName("updatedTime")
    var updatedTime : String,

    @SerializedName("nickname")
    var nickname : String,
)
