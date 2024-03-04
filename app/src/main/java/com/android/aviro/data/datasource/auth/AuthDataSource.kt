package com.android.aviro.data.datasource.auth

import com.android.aviro.data.model.auth.SignInRequest
import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.auth.TokensRequest
import com.android.aviro.data.model.auth.TokenResponse
import com.android.aviro.data.model.base.DataResponse

interface AuthDataSource { // 인증 // 인증 절차 수행 및 토큰 발급에 관여

    suspend fun getTokens(request: TokensRequest) : Result<DataResponse<TokenResponse>> //Any // remote로 부터 토큰 가져옴
    suspend fun requestSignIn(request: SignInRequest) : Result<DataResponse<SignInResponse>>

}