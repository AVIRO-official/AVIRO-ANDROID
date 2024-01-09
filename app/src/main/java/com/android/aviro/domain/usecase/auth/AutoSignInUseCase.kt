package com.android.aviro.domain.usecase.auth

import com.android.aviro.data.entity.auth.AuthResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository: AuthRepository
)  {

    // 자동로그인
    suspend operator fun invoke() : Boolean {
        if(authRepository.getTokensFromLocal()) {
            // 자동 로그인 진행 완료
            return true
        } else {
            // 자동 로그인 진행 실패
            return false
        }

    }

    suspend fun createSocialToken(id_token : String, auth_code : String) : Result<Any> {
        val response = authRepository.createTokenFromRemote(id_token, auth_code)
        response.onSuccess {
            when(it) {
                is AuthResponseDTO -> {
                    // 소셜 토큰, 사용자 기본 정보 저장
                    authRepository.saveTokenToLocal(it.accessToken, it.refreshToken, it.userId)
                }

            }

        }.onFailure {

        }

        return response


    }

}