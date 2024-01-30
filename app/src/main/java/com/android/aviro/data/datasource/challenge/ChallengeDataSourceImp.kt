package com.android.aviro.data.datasource.challenge

import com.android.aviro.data.api.AuthService
import com.android.aviro.data.api.ChallengeService
import com.android.aviro.data.entity.challenge.ChallengeInfoResponse
import javax.inject.Inject

class ChallengeDataSourceImp @Inject constructor(
    private val challengeService : ChallengeService
) : ChallengeDataSource {

    // 챌린지 기간, 이름
    override suspend fun getChallengeInfo() : Result<ChallengeInfoResponse> {
        return challengeService.getChallengeInfo()
    }
}