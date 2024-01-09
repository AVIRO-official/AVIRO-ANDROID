package com.android.aviro.domain.repository

import com.android.aviro.data.entity.member.NicknameEntity
import com.android.aviro.data.entity.member.NicknameCheckResponse

interface MemberRepository {

    suspend fun getMemberInfoFromLocal(key : String) : String?
    suspend fun saveMemberInfoToLocal(user_id : Int, user_name : String, user_email : String, nickname : String)

    suspend fun creatMember(nickname: String, birth : Int?, gender : String?, marketingAgree : Int) : Result<Any>
    suspend fun checkNickname(nickname : NicknameEntity) :  Result<NicknameCheckResponse>

}
