package com.aviro.android.domain.usecase.challenge

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.ChallengeRepository
import javax.inject.Inject

class GetChallengeInfo @Inject constructor (
    private val challengeRepository : ChallengeRepository
) {

    // 챌린지 정보
    suspend fun getChallengeInfo() : MappingResult {
        return challengeRepository.getChallengeInfo()
    }


    suspend fun getChallengeComment() : MappingResult {
        return challengeRepository.getChallengeComment()
    }

    suspend fun getChallengeImg() : MappingResult {
        return challengeRepository.getChallengePopUp()
    }

}