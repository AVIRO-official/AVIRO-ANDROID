package com.aviro.android.domain.entity.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class OperatingTime(
    val today : Boolean,
    val open : String,
    val breaktime : String
) : Parcelable
