package com.aviro.android.domain.usecase.member

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.MemberRepository
import javax.inject.Inject

class UpdateMyNicnameUseCase @Inject constructor (
    private val memberRepository : MemberRepository,
    private val getMyInfoUseCase : GetMyInfoUseCase
) {
    suspend operator fun invoke(new_nickname : String) : MappingResult {
        //val userId = memberRepository.getMemberInfoFromLocal("user_id")
        //return memberRepository.updateNickname(userId!!, new_nickname)
        val userId = getMyInfoUseCase.getUserId()
        userId.let {
            when (it) {
                is MappingResult.Success<*> -> return memberRepository.updateNickname(it.data as String, new_nickname)
                is MappingResult.Error -> return it
            }
        }
    }
}