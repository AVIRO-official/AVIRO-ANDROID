package com.aviro.android.data.api


import com.aviro.android.data.model.base.DataResponse
import com.aviro.android.data.model.challenge.ChallengeCommentResponse
import com.aviro.android.data.model.challenge.ChallengeInfoResponse
import com.aviro.android.data.model.challenge.ChallengePopUpResponse
import retrofit2.http.GET

interface ChallengeService {

    @GET("mypage/challenge")
    suspend fun getChallengeInfo(
    ): Result<DataResponse<ChallengeInfoResponse>>

    @GET("mypage/challenge/comment")
    suspend fun getChallengeComment(
    ): Result<DataResponse<ChallengeCommentResponse>>

    @GET("map/load/popup")
    suspend fun getChallengePopUp(
    ) : Result<DataResponse<ChallengePopUpResponse>>

}