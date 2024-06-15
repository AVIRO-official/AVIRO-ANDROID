package com.aviro.android.presentation.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewInfo(
    val commentId : String,
    val content : String
) : Parcelable
