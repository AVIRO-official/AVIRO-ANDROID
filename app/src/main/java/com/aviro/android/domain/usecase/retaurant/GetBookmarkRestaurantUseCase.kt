package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.RestaurantRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import javax.inject.Inject


class GetBookmarkRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository,
    private val getMyInfoUseCase : GetMyInfoUseCase
){
    operator suspend fun invoke(): MappingResult {
        // 서버 데이터와 동기화 -> 마커 업데이트 -> 마커 가져옴 (사용자 입장에서는 가게 데이터 가져온 것)

        val userId = getMyInfoUseCase.getUserId()
        userId.let { user_id ->
            when (user_id) {
                is MappingResult.Success<*> -> {
                    return restaurantRepository.getBookmarkRestaurant(user_id.data as String)
                }

                is MappingResult.Error -> {
                    return userId
                }
            }
        }
    }

}