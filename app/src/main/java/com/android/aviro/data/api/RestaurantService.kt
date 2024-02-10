package com.android.aviro.data.api

import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.restaurant.*
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

    @GET("map/check/place")
    suspend fun checkIsRegister(
        @Query("title") title : String,
        @Query("address") address : String,
        @Query("x") x : Double,
        @Query("y") y : Double
    ): Result<checkIsRestaurantResponse>

    @GET("map/load/bookmark")
    suspend fun getBookmarkList(
        @Query("userId") userId : String //UserIdEntity
    ): Result<BookmarkResponse>

    @GET("map/load/summary")
    suspend fun getRestaurantSummary(
        @Query("placeId") placeId : String
    ): Result<DataBodyResponse<RestaurantSummary>>

    @GET("map/load/place")
    suspend fun getRestaurantInfo(
        @Query("placeId") placeId : String
    ): Result<DataBodyResponse<RestaurantInfo>>

    @GET("map/load/menu")
    suspend fun getRestaurantMenu(
        @Query("placeId") placeId : String
    ): Result<DataBodyResponse<RestaurantMenu>>

    @GET("map/load/comment")
    suspend fun getRestaurantReview(
        @Query("placeId") placeId : String
    ): Result<DataBodyResponse<RestaurantReview>>

    /*
     @GET("map/load/timetable")
    suspend fun getRestaurantTimetable(
        @Query("placeId") placeId : String
    ): Result<BookmarkResponse>
    */

}