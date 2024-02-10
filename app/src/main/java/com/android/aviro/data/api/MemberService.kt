package com.android.aviro.data.api

import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.member.*
import com.android.aviro.data.entity.member.MemberEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberService {

    @POST("member/check")
    suspend fun checkNickname(
        @Body nickname: NicknameEntity
    ): Result<NicknameCheckResponse>

    @POST("member/sign-up")
    suspend fun createMember(
        @Body new_member: MemberEntity
    ): Result<DataBodyResponse<SignResponseDTO>>

    @GET("mypage/count")
    suspend fun getCount(
        @Query("userId") user_id: String //UserIdEntity
    ): Result<DataBodyResponse<MyInfoCountResponse>>

    @GET("mypage/challenge/level")
    suspend fun getChallengeLevel(
        @Query("userId") user_id : String //UserIdEntity
    ): Result<MyInfoLevelResponse>

    @POST("member/revoke")
    suspend fun deleteUser(
        @Body refreshToken: String
    ): Result<BaseResponse>

    @POST("member/update/nickname")
    suspend fun updateNickname(
        @Body request: NicknameUpdateRequest
    ): Result<BaseResponse>

}