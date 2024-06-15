package com.aviro.android.data.model.review

// DTO는 보통 request, response 모두 사용할 수 있을때 네이밍
data class ReviewDTO(

    val commentId : String,
    var userId : String,
    var content : String,
    var updatedTime : String,
    var nickname : String,
)
