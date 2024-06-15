package com.aviro.android.data.model.member

data class UpdateReviewRequest(
    val commentId : String,
    val userId : String,
    val content : String
)
