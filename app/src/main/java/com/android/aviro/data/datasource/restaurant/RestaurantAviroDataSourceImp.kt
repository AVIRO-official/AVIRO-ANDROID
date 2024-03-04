package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.api.KakaoService
import com.android.aviro.data.api.RestaurantService
import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.restaurant.*
import com.android.aviro.data.model.search.RestaurantVeganTypeRequest
import javax.inject.Inject


class RestaurantAviroDataSourceImp @Inject constructor (
    private val restaurantService: RestaurantService
) : RestaurantAviroDataSource {

    override suspend fun getRestaurant(request : ReataurantListRequest) : Result<DataResponse<ReataurantListReponse>>  {
        return restaurantService.getRestaurant(request.x,request.y,request.wide,request.time)
        }

    override suspend fun getRestaurantSummary(placeId : String) : Result<DataResponse<RestaurantSummaryResponse>> {
        return restaurantService.getRestaurantSummary(placeId)
    }


    override suspend fun getVeganTypeOfSearching(request : RestaurantVeganTypeRequest) : Result<DataResponse<RestaurantVeganTypeResponse>> {
        return restaurantService.getVeganOfPlace(request)
    }

    override suspend fun getBookmarkRestaurant(request : String) : Result<DataResponse<BookmarkResponse>> { //UserIdEntity
        return restaurantService.getBookmarkList(request)
    }

    }

