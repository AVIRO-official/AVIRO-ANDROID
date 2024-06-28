package com.aviro.android.domain.usecase.member

import android.util.Log
import com.aviro.android.domain.entity.auth.Sign
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.ACCESS_TOKEN_KEY
import com.aviro.android.domain.entity.key.REFRESH_TOKEN_KEY
import com.aviro.android.domain.entity.key.USER_EMAIL_KEY
import com.aviro.android.domain.entity.key.USER_ID_KEY
import com.aviro.android.domain.entity.key.USER_NAME_KEY
import com.aviro.android.domain.entity.key.USER_NICKNAME_KEY
import com.aviro.android.domain.entity.member.Member
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.MemberRepository
import com.aviro.android.domain.usecase.auth.GetTokenUseCase
import javax.inject.Inject

// 사용자 생성
class CreateMemberUseCase @Inject constructor (
    private val getTokenUseCase : GetTokenUseCase,
    private val memberRepository: MemberRepository,
    private val authRepository: AuthRepository
    ) {

    suspend operator fun invoke(userId : String, name : String, email : String, type : String, nickname: String, birth : Int?, gender : String?, marketingAgree : Boolean) : MappingResult  {
        //val signType = authRepository.getSignTypeFromLocal()
        val tokens = getTokenUseCase()[0]
        var userName = name //memberRepository.getMemberInfoFromLocal(USER_NAME_KEY)

        if(userName == null) {
            userName = ""
        }

        val response = memberRepository.creatMember(Member(tokens.get(REFRESH_TOKEN_KEY) ,tokens.get(
            ACCESS_TOKEN_KEY), userId, name, email, nickname, birth, gender, marketingAgree, type))

        when(response){
            is MappingResult.Success<*> -> {
                response.let {
                    val data = it.data as Sign
                    Log.d("Member","새로운 멤버 정보 저장 : ${data}")

                    memberRepository.saveMemberInfoToLocal(USER_ID_KEY,data.userId ) //data.userId //userId
                    memberRepository.saveMemberInfoToLocal(USER_NAME_KEY, userName)  //
                    memberRepository.saveMemberInfoToLocal(USER_EMAIL_KEY, data.userEmail) //data.userEmail //email
                    memberRepository.saveMemberInfoToLocal(USER_NICKNAME_KEY, data.nickname) //data.nickname //nickname
                    authRepository.saveSignTypeToLocal(type)

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