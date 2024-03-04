package com.android.aviro.data.api

import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.challenge.ChallengeInfoResponse
import retrofit2.http.GET

interface ChallengeService {

    @GET("mypage/challenge")
    suspend fun getChallengeInfo(
    ): Result<DataResponse<ChallengeInfoResponse>>
}