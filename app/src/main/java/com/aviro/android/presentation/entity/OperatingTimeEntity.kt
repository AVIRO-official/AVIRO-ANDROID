package com.aviro.android.presentation.entity

//@Parcelize
data class OperatingTimeEntity (
    var day : String,
    var isHoliday : Boolean,
    var isBreak : Boolean,
    var isAllHours : Boolean,
    var openTime : String,
    var closeTime : String,
    var breakStartTime : String,
    var breakEndTime : String
) //: Parcelable



