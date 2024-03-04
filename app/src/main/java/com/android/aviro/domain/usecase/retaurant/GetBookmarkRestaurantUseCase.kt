package com.android.aviro.domain.usecase.retaurant

import android.util.Log
import com.android.aviro.data.model.restaurant.BookmarkResponse
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.MemberRepository
import com.android.aviro.domain.repository.RestaurantRepository
import javax.inject.Inject


class GetBookmarkRestaurantUseCase @Inject constructor (
    private val restaurantRepository: RestaurantRepository,
    private val memberRepository : MemberRepository
){
    operator suspend fun invoke(): MappingResult {
        // 서버 데이터와 동기화 -> 마커 업데이트 -> 마커 가져옴 (사용자 입장에서는 가게 데이터 가져온 것)
        val userId = memberRepository.getMemberInfoFromLocal("user_id")
        Log.d("userId","${userId}" )
        return restaurantRepository.getBookmarkRestaurant(userId!!)
    }
}