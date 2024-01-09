package com.android.aviro.data.datasource.auth

import com.android.aviro.data.entity.auth.SignRequestDTO
import com.android.aviro.data.entity.auth.TokensRequestDTO

interface AuthDataSource { // 인증 // 인증 절차 수행 및 토큰 발급에 관여

    suspend fun getTokens(request: TokensRequestDTO) : Result<Any> // remote로 부터 토큰 가져옴
    suspend fun sign(request: SignRequestDTO) : Result<Any>
    suspend fun removeTokens(refresh_token : String)

}