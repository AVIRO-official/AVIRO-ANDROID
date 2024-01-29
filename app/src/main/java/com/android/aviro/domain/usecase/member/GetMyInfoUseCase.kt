package com.android.aviro.domain.usecase.member

import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.member.MyInfoCountResponse
import com.android.aviro.data.entity.member.MyInfoLevelResponse
import com.android.aviro.domain.repository.MemberRepository
import com.android.aviro.domain.usecase.retaurant.GetRestaurantUseCase
import javax.inject.Inject

class GetMyInfoUseCase  @Inject constructor (
    private val memberRepository : MemberRepository
) {

    // 사용자 기본 정보
    suspend fun getNickname() : String? {
       val nickname = memberRepository.getMemberInfoFromLocal("nickname")
        return nickname

    }

    // 사용자 가게 등록 정보 (활동 정보)
    suspend fun getCount() : Result<DataBodyResponse<MyInfoCountResponse>> {
        val userId = memberRepository.getMemberInfoFromLocal("user_id")
        return memberRepository.getCount(userId!!)

    }

    // 사용자 챌린지 정보 (활동 정보)
    suspend fun getChallengeLevel() : Result<MyInfoLevelResponse> {
        val userId = memberRepository.getMemberInfoFromLocal("user_id")
        return memberRepository.getChallengeLevel(userId!!)

    }
}