package com.aviro.android.data.datasource.auth

import com.aviro.android.data.model.auth.SignInRequest
import com.aviro.android.data.model.auth.SignInResponse
import com.aviro.android.data.model.auth.TokenResponse
import com.aviro.android.data.model.auth.TokensRequest
import com.aviro.android.data.model.auth.UserResponse
import com.aviro.android.data.model.base.DataResponse

interface AuthDataSource { // 인증 // 인증 절차 수행 및 토큰 발급에 관여

    suspend fun getTokens(request: TokensRequest) : Result<DataResponse<TokenResponse>> //Any // remote로 부터 토큰 가져옴
    suspend fun requestSignIn(request: SignInRequest) : Result<DataResponse<SignInResponse>>
    suspend fun getUser(userId: String) : Result<DataResponse<UserResponse>>

}