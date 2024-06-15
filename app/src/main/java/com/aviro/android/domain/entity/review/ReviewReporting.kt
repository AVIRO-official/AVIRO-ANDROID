package com.aviro.android.domain.entity.review

data class ReviewReporting(
    val commentId : String,
    val title : String,
    val createdTime : String,
    val commentContent : String,
    val commentNickname : String,
    val userId : String,
    val nickname : String,
    val code : Int,
    val content : String?
)
