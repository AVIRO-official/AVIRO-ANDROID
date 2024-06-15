package com.aviro.android.domain.entity.member

data class MyComment(
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
