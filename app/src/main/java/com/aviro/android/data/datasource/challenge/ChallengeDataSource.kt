package com.aviro.android.data.datasource.challenge

import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.challenge.ChallengeCommentResponse
import com.aviro.android.data.model.challenge.ChallengeInfoResponse
import com.aviro.android.data.model.challenge.ChallengePopUpResponse

interface ChallengeDataSource {
    suspend fun getChallengeInfo() : Result<DataResponse<ChallengeInfoResponse>>
    suspend fun getChallengeComment() : Result<DataResponse<ChallengeCommentResponse>>
    suspend fun getChallengePopUp() : Result<DataResponse<ChallengePopUpResponse>>

}