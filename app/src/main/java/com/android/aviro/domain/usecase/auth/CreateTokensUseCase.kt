package com.android.aviro.domain.usecase.auth

import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject

class CreateTokensUseCase  @Inject constructor (
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(id_token : String, auth_code : String) : Result<Any> {
        val response = authRepository.getTokensFromRemote(id_token, auth_code)
        response.onSuccess {
            when(it) {
                // 소셜 토큰 저장
                is TokensResponseDTO -> authRepository.saveTokenToLocal(it.accessToken, it.refreshToken)
            }

        }
        return response

    }
}