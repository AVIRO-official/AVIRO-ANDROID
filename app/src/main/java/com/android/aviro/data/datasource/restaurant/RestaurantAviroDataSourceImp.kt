package com.android.aviro.data.datasource.restaurant

import com.android.aviro.data.api.RestaurantService
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO
import javax.inject.Inject

class RestaurantAviroDataSourceImp @Inject constructor (
    private val restaurantService: RestaurantService
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

    }
