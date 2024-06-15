package com.aviro.android.data.model.restaurant

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule



@JsonInclude(JsonInclude.Include.NON_NULL)
data class RestaurantInfoUpdateRequest(
    val placeId: String,
    val userId: String,
    val nickname: String,
    val title: String,
    val changedTitle: ChangedString?,
    val category: ChangedString?,
    val address: ChangedString?,
    val address2: ChangedString?,
    val x: ChangedDouble?,
    val y: ChangedDouble?
)

fun filterNullValues(request: RestaurantInfoUpdateRequest): RestaurantInfoUpdateRequest {
    val mapper = jacksonObjectMapper().registerKotlinModule()
    val jsonString = mapper.writeValueAsString(request)
    val filteredRequest = mapper.readValue<RestaurantInfoUpdateRequest>(jsonString)//readValue<RestaurantInfoUpdateRequest>(jsonString)
    return filteredRequest
}