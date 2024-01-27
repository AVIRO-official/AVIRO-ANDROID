package com.android.aviro.domain.usecase.retaurant

import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.marker.MarkerEntity
import com.android.aviro.data.entity.marker.MarkerListEntity
import com.android.aviro.data.entity.restaurant.ReataurantReponseDTO
import com.android.aviro.data.entity.restaurant.RestaurantRequestDTO
import com.android.aviro.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
){

    // 가게 위치 데이터 업데이트 하기 -> 에러 처리
    operator suspend fun invoke(x : String, y : String, wide : String, time : String, isInitMap : Boolean): MappingResult {
        // 서버 데이터와 동기화 -> 마커 업데이트 -> 마커 가져옴 (사용자 입장에서는 가게 데이터 가져온 것)
       return restaurantRepository.getRestaurantLoc(x, y, wide, time, isInitMap)
    }

    // 마커 데이터 가져오기 -> 데이터 그려주기
    fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantReponseDTO) : List<MarkerEntity>? {
        return restaurantRepository.getMarker(isInitMap, reataurant_list)
    }


}