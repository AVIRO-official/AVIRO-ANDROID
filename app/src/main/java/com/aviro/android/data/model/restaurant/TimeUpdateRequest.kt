package com.aviro.android.data.model.restaurant

data class TimeUpdateRequest(
    val placeId : String,
    val userId : String,

    val mon : String,
    val monBreak : String,
    val tue : String,
    val tueBreak : String,
    val wed : String,
    val wedBreak : String,
    val thu : String,
    val thuBreak : String,
    val fri : String,
    val friBreak : String,
    val sat : String,
    val satBreak : String,
    val sun : String,
    val sunBreak : String
)
