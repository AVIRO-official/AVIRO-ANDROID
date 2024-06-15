package com.aviro.android.domain.usecase.retaurant

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.MemberRepository
import com.aviro.android.domain.repository.RestaurantRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import javax.inject.Inject

class GetRestaurantDetailUseCase @Inject constructor (
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val restaurantRepository: RestaurantRepository,
    private val memberRepository : MemberRepository
) {

    // 가게 위치 데이터 업데이트 하기 -> 에러 처리
    suspend fun getSummary(place_id : String) : MappingResult {
        // 서버 데이터와 동기화 -> 마커 업데이트 -> 마커 가져옴 (사용자 입장에서는 가게 데이터 가져온 것)

        //val user_id = memberRepository.getMemberInfoFromLocal(USER_ID_KEY)

        val userId = getMyInfoUseCase.getUserId()
        userId.let { user_id ->
            when (user_id) {
                is MappingResult.Success<*> -> {
                    return restaurantRepository.getRestaurantSummary(place_id, user_id.data as String)
                }

                is MappingResult.Error -> {
                    return user_id
                }
            }
        }

    }


    suspend fun getInfo(place_id : String) : MappingResult{
        return restaurantRepository.getRestaurantInfo(place_id)

    }

    suspend fun getMenu(place_id : String) : MappingResult{
        return restaurantRepository.getRestaurantMenu(place_id)
    }

    suspend fun getReview(place_id : String) : MappingResult{
        return restaurantRepository.getRestaurantReview(place_id)
    }

    suspend fun getTimetable(place_id : String) : MappingResult {
        return restaurantRepository.getRestaurantTimeTable(place_id)
    }


}