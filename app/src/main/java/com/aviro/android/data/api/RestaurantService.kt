package com.aviro.android.data.api

import com.android.aviro.data.model.restaurant.RestaurantVeganTypeResponse
import com.aviro.android.data.model.base.BaseResponse
import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.member.MemberLevelUpResponse
import com.aviro.android.data.model.restaurant.*
import com.aviro.android.data.model.review.ReportReviewRequest
import com.aviro.android.data.model.search.RestaurantVeganTypeRequest
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
        @Query("placeId") placeId : String,
        @Query("userId") userId : String
    ): Result<DataResponse<RestaurantSummaryResponse>>
    //@Body request : RestaurantSummaryRequest

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

     @GET("map/load/timetable")
    suspend fun getRestaurantTimetable(
        @Query("placeId") placeId : String
    ): Result<DataResponse<RestaurantTimetableResponse>>

    @POST("map/add/place")
    suspend fun createRestaurant(
        @Body request : RestaurantRequest
    ): Result<DataResponse<MemberLevelUpResponse>>

    @POST("map/add/comment")
    suspend fun createRestaurantReview(
        @Body request : RestaurantReviewAddRequest
    ): Result<DataResponse<MemberLevelUpResponse>>

    @POST("map/report/place")
    suspend fun reportRestaurant(
        @Body request : RestaurantReportRequest
    ): Result<BaseResponse>

    @POST("map/add/recommend")
    suspend fun recommendRestaurant(
        @Body request : RestaurantRecommendRequest
    ): Result<BaseResponse>

    @POST("map/report/comment")
    suspend fun reportReview(
        @Body request : ReportReviewRequest
    ): Result<BaseResponse>

    @POST("map/update/time")
    suspend fun updateTimetable(
        @Body request : TimeUpdateRequest
    ): Result<BaseResponse>
    @POST("map/update/menu")
    suspend fun updateMenu(
        @Body request : MenuUpdateRequest
    ): Result<BaseResponse>
    @POST("map/report/phone")
    suspend fun updatePhone(
        @Body request : PhoneUpdateRequest
    ): Result<BaseResponse>
    @POST("map/report/url")
    suspend fun updateHomepageUrl(
        @Body request : HomepageUpdateRequest
    ): Result<BaseResponse>

    @POST("map/report/address")
    suspend fun updateRestaurantInfo(
        @Body request : MutableMap<String, Any> //RestaurantInfoUpdateRequest
    ): Result<BaseResponse>

}