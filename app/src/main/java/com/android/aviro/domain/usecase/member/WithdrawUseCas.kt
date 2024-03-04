package com.android.aviro.domain.usecase.member

import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject

class WithdrawUseCas  @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository : AuthRepository,
    private val memberRepository : MemberRepository
) {

    suspend fun invoke() {
        val refresh_token = authRepository.getTokensFromLocal()[0].get("refresh_token")

        // remote에서 제거 // 유저 정보 제거
        memberRepository.deleteMember(refresh_token!!)
        // 로컬에서 제거
        authRepository.removeTokens()
    }
}