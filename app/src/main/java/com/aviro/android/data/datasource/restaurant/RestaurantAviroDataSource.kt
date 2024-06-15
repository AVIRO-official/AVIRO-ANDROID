package com.aviro.android.data.datasource.restaurant

import com.android.aviro.data.model.restaurant.RestaurantVeganTypeResponse
import com.aviro.android.data.model.base.BaseResponse
import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.member.MemberLevelUpResponse
import com.aviro.android.data.model.restaurant.*
import com.aviro.android.data.model.review.ReportReviewRequest
import com.aviro.android.data.model.search.RestaurantVeganTypeRequest

interface RestaurantAviroDataSource {

    suspend fun getRestaurant(request : ReataurantListRequest) : Result<DataResponse<ReataurantListReponse>>
    suspend fun getVeganTypeOfSearching(request : RestaurantVeganTypeRequest) : Result<DataResponse<RestaurantVeganTypeResponse>>
    suspend fun getIsRegistered(title : String, address : String, x :Double, y : Double) : Result<DataResponse<CheckingIsRegisterResponse>>
    suspend fun getBookmarkRestaurant(request : String) : Result<DataResponse<BookmarkResponse>> //UserIdEntity

    suspend fun getRestaurantSummary(placeId : String, userId : String) : Result<DataResponse<RestaurantSummaryResponse>>
    //request : RestaurantSummaryRequest
    suspend fun getRestaurantInfo(placeId : String) : Result<DataResponse<RestaurantInfoResponse>>
    suspend fun getRestaurantMenu(placeId : String) : Result<DataResponse<RestaurantMenuResponse>>
    suspend fun getRestaurantReview(placeId : String) : Result<DataResponse<RestaurantReviewResponse>>
    suspend fun getRestaurantTimeTable(placeId : String) : Result<DataResponse<RestaurantTimetableResponse>>

    suspend fun createRestaurant(request: RestaurantRequest) : Result<DataResponse<MemberLevelUpResponse>>
    suspend fun creatReview(request : RestaurantReviewAddRequest) : Result<DataResponse<MemberLevelUpResponse>>

    suspend fun reportRestaurant(request : RestaurantReportRequest) : Result<BaseResponse>

    suspend fun recommendRestaurant(request : RestaurantRecommendRequest) : Result<BaseResponse>

    suspend fun reportReview(request : ReportReviewRequest) : Result<BaseResponse>

    suspend fun updateTimetable(request : TimeUpdateRequest) : Result<BaseResponse>
    suspend fun updatePhone(request : PhoneUpdateRequest) : Result<BaseResponse>
    suspend fun updateHomepageUrl(request : HomepageUpdateRequest) : Result<BaseResponse>
    suspend fun updateMenu(request : MenuUpdateRequest) : Result<BaseResponse>
    suspend fun updateRestaurantInfo(request :MutableMap<String, Any>) : Result<BaseResponse> //RestaurantInfoUpdateRequest


}