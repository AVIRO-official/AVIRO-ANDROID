package com.android.aviro.domain.repository

import com.android.aviro.data.entity.challenge.ChallengeInfoResponse

/** 챌린지 정보와 관련된 데이터를 이 레포지토리에서 모아 처리합니다.
 *  유저의 챌린지 참여 정보는 MemberRepository에서 관리하고
 *  이 곳은 유저와는 상관없는 단순 챌린지 정보만 관리합니다.
 * */
interface ChallengeRepository {

    suspend fun getChallengeInfo() : Result<ChallengeInfoResponse>
    fun getChallengeComment()

}