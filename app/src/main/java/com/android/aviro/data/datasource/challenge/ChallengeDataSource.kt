package com.android.aviro.data.datasource.challenge

import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.challenge.ChallengeInfoResponse

interface ChallengeDataSource {
    suspend fun getChallengeInfo() : Result<DataResponse<ChallengeInfoResponse>>
}