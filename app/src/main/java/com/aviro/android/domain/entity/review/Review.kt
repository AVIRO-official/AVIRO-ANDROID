package com.aviro.android.domain.entity.review

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(
    val commentId : String,
    var userId : String,
    var content : String,
    var updatedTime : String,
    var nickname : String
) : Parcelable
