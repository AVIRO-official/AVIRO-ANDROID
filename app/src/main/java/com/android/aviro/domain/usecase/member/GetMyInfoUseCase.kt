package com.android.aviro.domain.usecase.member

import com.android.aviro.domain.repository.MemberRepository
import com.android.aviro.domain.usecase.retaurant.GetRestaurantUseCase
import javax.inject.Inject

class GetMyInfoUseCase  @Inject constructor (
    private val memberRepository : MemberRepository
) {

    // 사용자 기본 정보
    suspend fun getNickname() {
       val nickname = memberRepository.getMemberInfoFromLocal("nickname")

    }

    // 사용자 가게 등록 정보 (활동 정보)
    suspend fun getCount() {
        val userId = memberRepository.getMemberInfoFromLocal("user_id")

    }

    // 사용자 챌린지 정보 (활동 정보)
}