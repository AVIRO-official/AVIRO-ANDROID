package com.aviro.android.domain.usecase.auth

import com.aviro.android.domain.repository.AuthRepository
import javax.inject.Inject

/** 주로 autoSignInUseCase 에서 사용되는 세분화 된 UseCase 입니다.
 * Token 존재 여부에 따라 선제적으로 분기처리 해 불필요하게 autoSignInUseCase를 호출하지 않도록 하기 위해서
 * 세분화 하여 해당 UseCase를 사용합니다.
 * */
class GetTokenUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : List<Map<String, String?>> {
        val signType = authRepository.getSignTypeFromLocal()
        val tokens = authRepository.getTokensFromLocal()
        return tokens
    }

    suspend fun getTokenType() : String{
        return authRepository.getSignTypeFromLocal()
    }

}