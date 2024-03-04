package com.android.aviro.domain.usecase.member

import com.android.aviro.data.model.base.BaseResponse
import com.android.aviro.data.model.member.NicknameUpdateRequest
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject

class UpdateMyNicnameUseCase @Inject constructor (
    private val memberRepository : MemberRepository
) {
    suspend operator fun invoke(new_nickname : String) : MappingResult {
        val userId = memberRepository.getMemberInfoFromLocal("user_id")
        return memberRepository.updateNickname(userId!!, new_nickname)
    }
}