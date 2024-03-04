package com.android.aviro.domain.usecase.member

import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.challenge.ChallengeInfoResponse
import com.android.aviro.data.model.member.MemberHistoryResponse
import com.android.aviro.data.model.member.MemberLevelResponse
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.ChallengeRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject

class GetMyInfoUseCase  @Inject constructor (
    private val memberRepository : MemberRepository,
    private val challengeRepository : ChallengeRepository
) {

    // 사용자 기본 정보
    suspend fun getNickname() : String? {
       val nickname = memberRepository.getMemberInfoFromLocal("nickname")
        return nickname

    }

    // 사용자 가게 등록 정보 (활동 정보)
    suspend fun getCount() : MappingResult {
        val userId = memberRepository.getMemberInfoFromLocal("user_id")
        return memberRepository.getCount(userId!!)

    }

    // 사용자 챌린지 정보 (레벨 정보)
    suspend fun getChallengeLevel() : MappingResult {
        val userId = memberRepository.getMemberInfoFromLocal("user_id")
        return memberRepository.getChallengeLevel(userId!!)

    }

    // 챌린지 정보
    suspend fun getChallengeInfo() : MappingResult {
        return challengeRepository.getChallengeInfo()

    }
}