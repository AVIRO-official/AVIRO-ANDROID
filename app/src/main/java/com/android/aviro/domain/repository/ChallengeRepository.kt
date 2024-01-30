package com.android.aviro.domain.repository

import com.android.aviro.data.entity.challenge.ChallengeInfoResponse

interface ChallengeRepository {

    suspend fun getChallengeInfo() : Result<ChallengeInfoResponse>
    fun getChallengeComment()

}