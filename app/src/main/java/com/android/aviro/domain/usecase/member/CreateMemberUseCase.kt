package com.android.aviro.domain.usecase.member

import android.util.Log
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.member.NicknameEntity
import com.android.aviro.data.entity.member.NicknameCheckResponse
import com.android.aviro.data.repository.MemberRepositoryImp
import javax.inject.Inject

// 사용자 생성
class CreateMemberUseCase @Inject constructor (
    private val memberRepository: MemberRepositoryImp
    ) {

    suspend operator fun invoke(nickname: String, birth : Int?, gender : String?, marketingAgree : Int) : MappingResult  {
        val response = memberRepository.creatMember(nickname, birth, gender, marketingAgree)
        when(response){
            is MappingResult.Success<*> -> {
                response.let {
                    val data = it.data as SignResponseDTO
                    Log.d("Member","새로운 멤버 정보 저장 : ${data}")
                    memberRepository.saveMemberInfoToLocal("user_id",data.userId)
                    memberRepository.saveMemberInfoToLocal("user_name",data.userName)
                    memberRepository.saveMemberInfoToLocal("user_email",data.userEmail)
                    memberRepository.saveMemberInfoToLocal("nickname",data.nickname)
                }
            }
            is MappingResult.Error -> {}
        }
        return response

    }

    suspend fun checkNickname(nickname : String) : Result<NicknameCheckResponse> {
        return memberRepository.checkNickname(NicknameEntity(nickname))
    }

}