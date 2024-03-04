package com.android.aviro.data.api

import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.restaurant.*
import com.android.aviro.data.model.search.RestaurantVeganTypeRequest
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
    ): Result<DataResponse<ReataurantListReponse>>

    @POST("map/check/place")
    suspend fun getVeganOfPlace(
        @Body request : RestaurantVeganTypeRequest
        ): Result<DataResponse<RestaurantVeganTypeResponse>>

    @GET("map/check/place")
    suspend fun checkIsRegister(
        @Query("title") title : String,
        @Query("address") address : String,
        @Query("x") x : Double,
        @Query("y") y : Double
    ): Result<DataResponse<CheckingIsRegisterResponse>>

    @GET("map/load/bookmark")
    suspend fun getBookmarkList(
        @Query("userId") userId : String //UserIdEntity
    ): Result<DataResponse<BookmarkResponse>>

    @GET("map/load/summary")
    suspend fun getRestaurantSummary(
        @Query("placeId") placeId : String
    ): Result<DataResponse<RestaurantSummaryResponse>>

    @GET("map/load/place")
    suspend fun getRestaurantInfo(
        @Query("placeId") placeId : String
    ): Result<DataResponse<RestaurantInfoResponse>>

    @GET("map/load/menu")
    suspend fun getRestaurantMenu(
        @Query("placeId") placeId : String
    ): Result<DataResponse<RestaurantMenuResponse>>

    @GET("map/load/comment")
    suspend fun getRestaurantReview(
        @Query("placeId") placeId : String
    ): Result<DataResponse<RestaurantReviewResponse>>

    /*
     @GET("map/load/timetable")
    suspend fun getRestaurantTimetable(
        @Query("placeId") placeId : String
    ): Result<BookmarkResponse>
    */

}