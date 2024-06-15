package com.aviro.android.data.model.review

data class ReportReviewRequest(
    val commentId : String,
    val title : String,
    val createdTime : String,
    val commentContent : String,
    val commentNickname : String,
    val userId : String,
    val nickname : String, // 신고자
    val code : Int,
    val content : String?, // 신고 사유가 '기타(7)' 인 경우 사유
)
