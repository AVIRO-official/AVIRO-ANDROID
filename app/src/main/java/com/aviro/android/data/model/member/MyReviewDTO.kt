package com.aviro.android.data.model.member

data class MyReviewDTO(
    val commentId : String,
    val placeId : String,
    var content : String,
    val title: String = "",
    val category: String = "",
    val allVegan: Boolean = true,
    val someMenuVegan: Boolean = false,
    val ifRequestVegan: Boolean = false,
    val createdBefore : String = ""
)
