package com.android.aviro.data.api

import com.android.aviro.data.entity.challenge.ChallengeInfoResponse
import com.android.aviro.data.entity.member.MyInfoLevelResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengeService {

    @GET("mypage/challenge")
    suspend fun getChallengeInfo(
    ): Result<ChallengeInfoResponse>
}