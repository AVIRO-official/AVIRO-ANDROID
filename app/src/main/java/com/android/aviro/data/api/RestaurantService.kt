package com.android.aviro.data.api

import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO
import com.android.aviro.data.entity.restaurant.VeganOfSearchingRequest
import com.android.aviro.data.entity.restaurant.VeganOfSearchingResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface RestaurantService {
    @GET("map")
    suspend fun getRestaurant(
        @Query("x") x : String,
        @Query("y") y : String,
        @Query("wide") wide : String,
        @Query("time") time : String

    ): Result<DataBodyResponse<ReataurantReponseDTO>>

    @POST("map/check/place")
    suspend fun getVeganOfPlace(
        @Body request : VeganOfSearchingRequest
        ): Result<VeganOfSearchingResponse>

}