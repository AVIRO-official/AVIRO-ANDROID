package com.android.aviro.data.api

import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.member.*
import com.android.aviro.data.entity.member.MemberEntity
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
        @Body user_id: UserIdEntity
    ): Result<DataBodyResponse<MyInfoCountResponse>>

    @POST("member/challenge/level")
    suspend fun getChallengeLevel(
        @Body user_id: UserIdEntity
    ): Result<MyInfoLevelResponse>
}