package com.android.aviro.data.api

import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.base.BaseResponse
import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.member.*
import com.android.aviro.data.model.member.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberService {

    @POST("member/check")
    suspend fun checkNickname(
        @Body nickname: String
    ): Result<NicknameCheckResponse>

    @POST("member/sign-up")
    suspend fun createMember(
        @Body new_member: SignUpRequest
    ): Result<DataResponse<SignInResponse>>

    @GET("mypage/count")
    suspend fun getCount(
        @Query("userId") user_id: String //UserIdEntity
    ): Result<DataResponse<MemberHistoryResponse>>

    @GET("mypage/challenge/level")
    suspend fun getChallengeLevel(
        @Query("userId") user_id : String //UserIdEntity
    ): Result<DataResponse<MemberLevelResponse>>

    @POST("member/revoke")
    suspend fun deleteUser(
        @Body refreshToken: String
    ): Result<BaseResponse>

    @POST("member/update/nickname")
    suspend fun updateNickname(
        @Body request: NicknameUpdateRequest
    ): Result<BaseResponse>

}