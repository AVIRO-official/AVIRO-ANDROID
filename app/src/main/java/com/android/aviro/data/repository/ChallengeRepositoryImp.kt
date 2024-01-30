package com.android.aviro.data.repository

import com.android.aviro.data.api.ChallengeService
import com.android.aviro.data.datasource.challenge.ChallengeDataSource
import com.android.aviro.data.datasource.challenge.ChallengeDataSourceImp
import com.android.aviro.data.entity.challenge.ChallengeInfoResponse
import com.android.aviro.domain.repository.ChallengeRepository
import javax.inject.Inject

class ChallengeRepositoryImp @Inject constructor(
    private val challengeDataSource : ChallengeDataSource
) : ChallengeRepository {

    override suspend fun getChallengeInfo() : Result<ChallengeInfoResponse> {
        return challengeDataSource.getChallengeInfo()

    }
    override fun getChallengeComment() {

    }
}