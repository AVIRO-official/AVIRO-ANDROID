package com.android.aviro.domain.usecase.auth

import com.android.aviro.domain.repository.AuthRepository
import javax.inject.Inject

class GetTokenUseCase@Inject constructor (
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : String? {
        val token = authRepository.getTokensFromLocal()
        //if(token != null) return
        return token

    }
}