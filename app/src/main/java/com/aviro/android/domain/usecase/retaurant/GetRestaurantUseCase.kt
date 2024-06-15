package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
){

    // 가게 위치 데이터 업데이트 하기 -> 에러 처리
    operator suspend fun invoke(isInitMap : Boolean, x : String, y : String, wide : String, time : String): MappingResult {
        // 서버 데이터와 동기화 -> 마커 업데이트 -> 마커 가져옴 (사용자 입장에서는 가게 데이터 가져온 것)
       return restaurantRepository.getRestaurantLoc(isInitMap, x, y, wide, time)
    }

    /*
    // 마커 데이터 가져오기 -> 데이터 그려주기
    fun getMarker(isInitMap : Boolean, reataurant_list : ReataurantListReponse) : List<MarkerDAO>? {
        return restaurantRepository.getMarker(isInitMap, reataurant_list)
    }

    fun getMarker() : List<MarkerDAO>? { //bookmark_list : List<String>
        return restaurantRepository.getMarkerForBookmark() //bookmark_list
    }

     */


}