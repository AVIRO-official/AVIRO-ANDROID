package com.aviro.android.data.datasource.challenge

import com.aviro.android.data.api.ChallengeService
import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.challenge.ChallengeCommentResponse
import com.aviro.android.data.model.challenge.ChallengeInfoResponse
import com.aviro.android.data.model.challenge.ChallengePopUpResponse
import javax.inject.Inject

class ChallengeDataSourceImp @Inject constructor(
    private val challengeService : ChallengeService
) : ChallengeDataSource {

    // 챌린지 기간, 이름
    override suspend fun getChallengeInfo() : Result<DataResponse<ChallengeInfoResponse>> {
        return challengeService.getChallengeInfo()
    }
    override suspend fun getChallengeComment() : Result<DataResponse<ChallengeCommentResponse>> {
        return challengeService.getChallengeComment()
    }
    override suspend fun getChallengePopUp() : Result<DataResponse<ChallengePopUpResponse>> {
        return challengeService.getChallengePopUp()
    }

}