package com.aviro.android.data.datasource.restaurant

import com.android.aviro.data.model.restaurant.RestaurantVeganTypeResponse
import com.aviro.android.data.api.RestaurantService
import com.aviro.android.data.model.base.BaseResponse
import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.member.MemberLevelUpResponse
import com.aviro.android.data.model.restaurant.*
import com.aviro.android.data.model.review.ReportReviewRequest
import com.aviro.android.data.model.search.RestaurantVeganTypeRequest
import javax.inject.Inject


class RestaurantAviroDataSourceImp @Inject constructor (
    private val restaurantService: RestaurantService
) : RestaurantAviroDataSource {

    override suspend fun getRestaurant(request : ReataurantListRequest) : Result<DataResponse<ReataurantListReponse>>  {

        return restaurantService.getRestaurant(request.x,request.y,request.wide,request.time)
        }

    override suspend fun getVeganTypeOfSearching(request : RestaurantVeganTypeRequest) : Result<DataResponse<RestaurantVeganTypeResponse>> {
        return restaurantService.getVeganOfPlace(request)
    }

    override suspend fun getIsRegistered(title : String, address : String, x :Double, y : Double) : Result<DataResponse<CheckingIsRegisterResponse>> {
        return restaurantService.checkIsRegister(title, address, x, y)
    }

    override suspend fun getBookmarkRestaurant(request : String) : Result<DataResponse<BookmarkResponse>> { //UserIdEntity
        return restaurantService.getBookmarkList(request)
    }

    override suspend fun getRestaurantSummary(placeId : String, userId : String) : Result<DataResponse<RestaurantSummaryResponse>> {
        return restaurantService.getRestaurantSummary(placeId, userId) //request : RestaurantSummaryRequest
    }

    override suspend fun getRestaurantInfo(placeId : String) : Result<DataResponse<RestaurantInfoResponse>> {
        return restaurantService.getRestaurantInfo(placeId)
    }
    override suspend fun getRestaurantMenu(placeId : String) : Result<DataResponse<RestaurantMenuResponse>> {
        return restaurantService.getRestaurantMenu(placeId)

    }
    override suspend fun getRestaurantReview(placeId : String) : Result<DataResponse<RestaurantReviewResponse>> {
        return restaurantService.getRestaurantReview(placeId)

    }
    override suspend fun getRestaurantTimeTable(placeId : String) : Result<DataResponse<RestaurantTimetableResponse>> {
        return restaurantService.getRestaurantTimetable(placeId)
    }

    override suspend fun createRestaurant(request: RestaurantRequest) : Result<DataResponse<MemberLevelUpResponse>> {
        return restaurantService.createRestaurant(request)
    }
    override suspend fun creatReview(request : RestaurantReviewAddRequest) : Result<DataResponse<MemberLevelUpResponse>> {
        return restaurantService.createRestaurantReview(request)
    }

    override suspend fun reportRestaurant(request : RestaurantReportRequest) : Result<BaseResponse> {
        return restaurantService.reportRestaurant(request)
    }

    override suspend fun recommendRestaurant(request : RestaurantRecommendRequest) : Result<BaseResponse> {
        return restaurantService.recommendRestaurant(request)
    }
    override suspend fun reportReview(request : ReportReviewRequest) : Result<BaseResponse> {
        return restaurantService.reportReview(request)
    }

    override suspend fun updateTimetable(request : TimeUpdateRequest) : Result<BaseResponse> {
        return restaurantService.updateTimetable(request)
    }
    override suspend fun updatePhone(request : PhoneUpdateRequest) : Result<BaseResponse> {
        return restaurantService.updatePhone(request)
    }
    override suspend fun updateHomepageUrl(request : HomepageUpdateRequest) : Result<BaseResponse> {
        return restaurantService.updateHomepageUrl(request)
    }
    override suspend fun updateMenu(request : MenuUpdateRequest) : Result<BaseResponse> {
        return restaurantService.updateMenu(request)
    }
    override suspend fun updateRestaurantInfo(request :  MutableMap<String, Any>) : Result<BaseResponse> { //RestaurantInfoUpdateRequest
        return restaurantService.updateRestaurantInfo(request)
    }


    }

