package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.api.KakaoService
import com.android.aviro.data.api.RestaurantService
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.restaurant.*
import dagger.Provides
import javax.inject.Inject


class RestaurantAviroDataSourceImp @Inject constructor (
    private val restaurantService: RestaurantService,
    private val kakaoService: KakaoService
) : RestaurantAviroDataSource {

    override suspend fun getRestaurant(request : RestaurantRequestDTO) : Result<DataBodyResponse<ReataurantReponseDTO>>  {
        return restaurantService.getRestaurant(request.x,request.y,request.wide,request.time)
        /*
        response.onSuccess {
            val data = it.data
            if (data != null) {
                return Result.success(it.data)
            }
        }.onFailure {
            return Result.failure(IllegalStateException("알 수 없는 오류가 발생했습니다."))
        }
        return Result.failure(IllegalStateException("알 수 없는 오류가 발생했습니다."))

         */
        }

    override suspend fun getSearchedRestaurant(keyword : String, x : String, y : String, page : Int, size : Int, sort : String) : Result<SearchedPlaceListResponse> {
        return kakaoService.searchRestaurant(keyword, x, y, page, size, sort)
    }

    override suspend fun getVeganTypeOfSearching(request : VeganOfSearchingRequest) : Result<VeganOfSearchingResponse> {
        return restaurantService.getVeganOfPlace(request)
    }

    }

