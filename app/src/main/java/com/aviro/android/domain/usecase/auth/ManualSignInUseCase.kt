package com.aviro.android.domain.usecase.auth

import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.repository.AuthRepository
import javax.inject.Inject

class ManualSignInUseCase @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository: AuthRepository,

) {

    // 수동 로그인 (회원가입 여부, 닉네임)
    suspend operator fun invoke(userId : String): MappingResult {
        return authRepository.getUser(userId)
    }
}