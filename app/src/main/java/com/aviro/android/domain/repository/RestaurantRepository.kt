package com.aviro.android.domain.repository


import com.aviro.android.data.model.marker.MarkerDAO
import com.aviro.android.data.model.restaurant.*
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.restaurant.MenuUpdating
import com.aviro.android.domain.entity.restaurant.PhoneUpdating
import com.aviro.android.domain.entity.restaurant.ReportRestaurant
import com.aviro.android.domain.entity.restaurant.Restaurant
import com.aviro.android.domain.entity.restaurant.TimetableUpdating
import com.aviro.android.domain.entity.restaurant.UrlUpdating
import com.aviro.android.domain.entity.review.ReviewAdding
import com.aviro.android.domain.entity.review.ReviewReporting

interface RestaurantRepository {

    suspend fun getRestaurantLoc(isInitMap : Boolean, x : String, y : String, wide : String, time : String) : MappingResult
    fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantListReponse) : List<MarkerDAO>?
    //fun getMarkerForBookmark() : List<MarkerDAO>? //bookmark_list : List<String>

    suspend fun searchRestaurant(keyword : String, x : String?, y : String?, page : Int, size : Int, sort : String) : MappingResult //Result<SearchedPlaceListResponse>
    suspend fun getVeganTypeOfSearching(isEnd : Boolean, SearchedPlaceRawList : List<Document>) : MappingResult
    suspend fun getIsRegistered(title : String, address : String, x : Double, y :Double) : MappingResult
    suspend fun getBookmarkRestaurant(userId : String) : MappingResult

    suspend fun getRestaurantSummary(placeId : String,  userId: String) : MappingResult
    suspend fun getRestaurantInfo(placeId : String) : MappingResult
    suspend fun getRestaurantMenu(placeId : String) : MappingResult
    suspend fun getRestaurantReview(placeId : String) : MappingResult
    suspend fun getRestaurantTimeTable(placeId : String) : MappingResult

    suspend fun createRestaurant(restaurant : Restaurant) : MappingResult
    suspend fun createRestaurantReview(review : ReviewAdding) : MappingResult

    suspend fun reportRestaurant(report : ReportRestaurant) : MappingResult

    suspend fun recommendRestaurant(placeId: String, userId: String, isRecommend : Boolean) : MappingResult

    suspend fun reportReview(reviewId: ReviewReporting) : MappingResult

    suspend fun updatePhone(updating: PhoneUpdating) : MappingResult
    suspend fun updateUrl(updating: UrlUpdating) : MappingResult
    suspend fun updateTimetable(placeId : String, userId : String, updating : TimetableUpdating) : MappingResult

    suspend fun updateMenus(userId : String, updating : MenuUpdating) : MappingResult

    suspend fun updateRestaurantInfo(updating : MutableMap<String, Any>) : MappingResult //RestaurantInfoUpdating

    // 공공api로 주소찾기
    suspend fun searchPublicAddress(keyword :String, currentPage : Int) : MappingResult

    // 공공api로 찾은 주소의 좌표 찾기
    suspend fun getCoordinationOfAddress(keyword : String) : MappingResult

    // 좌표로 도로명 찾기
    suspend fun getAddressOfCoordination(x : String, y : String) : MappingResult


}