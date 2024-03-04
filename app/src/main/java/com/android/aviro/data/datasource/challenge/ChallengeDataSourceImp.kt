package com.android.aviro.data.datasource.challenge

import com.android.aviro.data.api.ChallengeService
import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.challenge.ChallengeInfoResponse
import javax.inject.Inject

class ChallengeDataSourceImp @Inject constructor(
    private val challengeService : ChallengeService
) : ChallengeDataSource {

    // 챌린지 기간, 이름
    override suspend fun getChallengeInfo() : Result<DataResponse<ChallengeInfoResponse>> {
        return challengeService.getChallengeInfo()
    }
}