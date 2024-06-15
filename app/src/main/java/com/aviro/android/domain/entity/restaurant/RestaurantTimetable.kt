package com.aviro.android.domain.entity.restaurant

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RestaurantTimetable (
        val mon: OperatingTime,
        val tue: OperatingTime,
        val wed: OperatingTime,
        val thu: OperatingTime,
        val fri: OperatingTime,
        val sat: OperatingTime,
        val sun: OperatingTime
        ) : Parcelable