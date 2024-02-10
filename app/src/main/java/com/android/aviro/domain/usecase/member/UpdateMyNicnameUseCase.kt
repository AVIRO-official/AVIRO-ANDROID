package com.android.aviro.domain.usecase.member

import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.data.entity.member.NicknameUpdateRequest
import com.android.aviro.domain.repository.ChallengeRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject

class UpdateMyNicnameUseCase @Inject constructor (
    private val memberRepository : MemberRepository
) {
    suspend operator fun invoke(new_nickname : String) :  Result<BaseResponse> {
        val userId = memberRepository.getMemberInfoFromLocal("user_id")
        val response = memberRepository.updateNickname(NicknameUpdateRequest(userId!!, new_nickname))
        response.onSuccess {
            val code = it.statusCode
            if(code == 200) {
                // 로컬에 반영
                memberRepository.saveMemberInfoToLocal("nickname", new_nickname)
            }
        }
        return response
    }
}