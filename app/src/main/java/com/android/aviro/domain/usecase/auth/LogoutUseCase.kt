package com.android.aviro.domain.usecase.auth

import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository : AuthRepository
)  {
    // 로그아웃
    suspend fun invoke() {
        // 로컬에서만 제거
        authRepository.removeTokens()
        // 유저 정보도 제거해야 하나?
    }
}