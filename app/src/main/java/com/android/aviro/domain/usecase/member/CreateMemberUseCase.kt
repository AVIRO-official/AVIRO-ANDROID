package com.android.aviro.domain.usecase.member

import android.util.Log
import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.member.NicknameCheckResponse
import com.android.aviro.data.repository.MemberRepositoryImp
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.entity.member.Member
import com.android.aviro.domain.usecase.auth.GetTokenUseCase
import javax.inject.Inject

// 사용자 생성
class CreateMemberUseCase @Inject constructor (
    private val getTokenUseCase : GetTokenUseCase,
    private val memberRepository: MemberRepositoryImp
    ) {

    suspend operator fun invoke(nickname: String, birth : Int?, gender : String?, marketingAgree : Boolean) : MappingResult  {
        val tokens = getTokenUseCase()[0]
        val response = memberRepository.creatMember(Member(tokens.get("refresh_token")!!,tokens.get("access_token")!!,
            memberRepository.getMemberInfoFromLocal("user_id")!!, memberRepository.getMemberInfoFromLocal("user_name"), memberRepository.getMemberInfoFromLocal("user_email")!!,
            nickname, birth, gender, marketingAgree))
        when(response){
            is MappingResult.Success<*> -> {
                response.let {
                    val data = it.data as SignInResponse
                    Log.d("Member","새로운 멤버 정보 저장 : ${data}")
                    //memberRepository.saveMemberInfoToLocal("user_id",data.userId)
                    //memberRepository.saveMemberInfoToLocal("user_name",data.userName)
                    //memberRepository.saveMemberInfoToLocal("user_email",data.userEmail)
                    memberRepository.saveMemberInfoToLocal("nickname",data.nickname)
                }
            }
            is MappingResult.Error -> {}
        }
        return response

    }

    suspend fun checkNickname(nickname : String) : MappingResult {
        return memberRepository.checkNickname(nickname)
    }

}