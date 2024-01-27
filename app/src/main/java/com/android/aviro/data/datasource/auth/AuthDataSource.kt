package com.android.aviro.data.datasource.auth

import com.android.aviro.data.entity.auth.SignRequestDTO
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.DataBodyResponse

interface AuthDataSource { // 인증 // 인증 절차 수행 및 토큰 발급에 관여

    suspend fun getTokens(request: TokensRequestDTO) : Result<DataBodyResponse<TokensResponseDTO>> //Any // remote로 부터 토큰 가져옴
    suspend fun requestSignIn(request: SignRequestDTO) : Result<DataBodyResponse<SignResponseDTO>>
    suspend fun removeTokens(refresh_token : String)

}