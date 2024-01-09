package com.android.aviro.domain.usecase.auth

import com.android.aviro.domain.repository.AuthRepository
import javax.inject.Inject

class AutoSignInUseCase @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository: AuthRepository
)  {

    // 자동로그인
    suspend operator fun invoke() : Boolean {
        return authRepository.getTokensFromLocal()
    }

}