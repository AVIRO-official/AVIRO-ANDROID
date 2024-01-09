package com.android.aviro.domain.usecase.member

import com.android.aviro.data.entity.member.NicknameEntity
import com.android.aviro.data.entity.member.NicknameCheckResponse
import com.android.aviro.data.repository.MemberRepositoryImp
import javax.inject.Inject

// 사용자 생성
class CreateMemberUseCase @Inject constructor (
    private val memberRepository: MemberRepositoryImp
    ) {

    suspend operator fun invoke(nickname: String, birth : Int?, gender : String?, marketingAgree : Int) : Result<Any> {
        return memberRepository.creatMember(nickname, birth, gender, marketingAgree)

    }

    suspend fun checkNickname(nickname : String) : Result<NicknameCheckResponse> {
        return memberRepository.checkNickname(NicknameEntity(nickname))

    }


}