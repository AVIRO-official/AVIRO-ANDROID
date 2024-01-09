package com.android.aviro.data.datasource.member

import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.member.NicknameEntity
import com.android.aviro.data.entity.member.NicknameCheckResponse
import com.android.aviro.domain.entity.MemberEntity

interface MemberDataSource {

    suspend fun checkNickname(nickname : NicknameEntity) :  Result<NicknameCheckResponse>
    suspend fun creatMember(newMember : MemberEntity) : Result<Any>
}