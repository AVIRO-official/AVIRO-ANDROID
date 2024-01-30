package com.android.aviro.domain.repository

import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.member.MyInfoCountResponse
import com.android.aviro.data.entity.member.MyInfoLevelResponse
import com.android.aviro.data.entity.member.NicknameEntity
import com.android.aviro.data.entity.member.NicknameCheckResponse

interface MemberRepository {
    suspend fun getMemberInfoFromLocal(key : String) : String?
    suspend fun saveMemberInfoToLocal(key: String, value : String)

    suspend fun creatMember(nickname: String, birth : Int?, gender : String?, marketingAgree : Int) : MappingResult //Result<Any>
    suspend fun checkNickname(nickname : NicknameEntity) :  Result<NicknameCheckResponse>

    suspend fun getCount(userId : String) :  Result<DataBodyResponse<MyInfoCountResponse>>
    suspend fun getChallengeLevel(userId : String) : Result<MyInfoLevelResponse>
}
