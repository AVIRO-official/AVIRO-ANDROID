package com.android.aviro.data.datasource.challenge

import com.android.aviro.data.entity.challenge.ChallengeInfoResponse

interface ChallengeDataSource {

    suspend fun getChallengeInfo() : Result<ChallengeInfoResponse>
}