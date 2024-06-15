package com.aviro.android.data.model.restaurant


data class RestaurantTimetableResponse(
    val mon: OperatingTimeDTO,
    val tue: OperatingTimeDTO,
    val wed: OperatingTimeDTO,
    val thu: OperatingTimeDTO,
    val fri: OperatingTimeDTO,
    val sat: OperatingTimeDTO,
    val sun: OperatingTimeDTO

)
