package com.android.aviro.data.model.review

import com.google.gson.annotations.SerializedName

// DTO는 보통 request, response 모두 사용할 수 있을때 네이밍
data class ReviewDTO(

    val commentId : String,
    var userId : String,
    var content : String,
    var updatedTime : String,
    var nickname : String,
)
