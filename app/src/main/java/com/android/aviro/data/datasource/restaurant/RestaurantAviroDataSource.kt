package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.restaurant.*
import com.android.aviro.data.model.search.RestaurantVeganTypeRequest

interface RestaurantAviroDataSource {

    suspend fun getRestaurant(request : ReataurantListRequest) : Result<DataResponse<ReataurantListReponse>>
    suspend fun getRestaurantSummary(placeId : String) : Result<DataResponse<RestaurantSummaryResponse>>
    suspend fun getVeganTypeOfSearching(request : RestaurantVeganTypeRequest) : Result<DataResponse<RestaurantVeganTypeResponse>>
    suspend fun getBookmarkRestaurant(request : String) : Result<DataResponse<BookmarkResponse>> //UserIdEntity

}