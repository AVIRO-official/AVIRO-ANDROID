package com.android.aviro.domain.entity.review

data class Review(
    val commentId : String,
    var userId : String,
    var content : String,
    var updatedTime : String,
    var nickname : String,
)
