package com.android.aviro.domain.usecase.retaurant

import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.RestaurantRepository
import javax.inject.Inject

class GetRestaurantDetailUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository
) {

    // 가게 위치 데이터 업데이트 하기 -> 에러 처리
    suspend fun getSummary(place_id : String) : MappingResult {
        // 서버 데이터와 동기화 -> 마커 업데이트 -> 마커 가져옴 (사용자 입장에서는 가게 데이터 가져온 것)
        return restaurantRepository.getRestaurantSummary(place_id)
    }

    /*
    suspend fun getInfo(place_id : String) {

    }

    suspend fun getMenu(place_id : String) {

    }

    suspend fun getReview(place_id : String) {

    }

     */
}