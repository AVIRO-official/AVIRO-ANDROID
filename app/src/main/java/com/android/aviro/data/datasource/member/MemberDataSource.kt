package com.android.aviro.data.datasource.member

import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.member.*
import com.android.aviro.data.entity.member.MemberEntity

interface MemberDataSource {

    suspend fun checkNickname(nickname : NicknameEntity) :  Result<NicknameCheckResponse>
    suspend fun creatMember(newMember : MemberEntity) : Result<DataBodyResponse<SignResponseDTO>> // Result<Any>
    suspend fun getActivityCount(requet : String) : Result<DataBodyResponse<MyInfoCountResponse>>
    suspend fun getChallengeLevel(requet : String) : Result<MyInfoLevelResponse>
}