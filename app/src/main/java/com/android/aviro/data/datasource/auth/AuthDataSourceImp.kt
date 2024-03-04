package com.android.aviro.data.datasource.auth

import com.android.aviro.data.api.AuthService
import com.android.aviro.data.model.auth.SignInRequest
import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.auth.TokensRequest
import com.android.aviro.data.model.auth.TokenResponse
import com.android.aviro.data.model.base.DataResponse
import javax.inject.Inject


class AuthDataSourceImp @Inject constructor(
    private val authService : AuthService
) : AuthDataSource {

    // 서버로 refresh token 요청
    override suspend fun getTokens(request: TokensRequest) : Result<DataResponse<TokenResponse>> {
        return authService.getTokens(request)

    }

    // 서버로 로그인 요청
    override suspend fun requestSignIn(request: SignInRequest) : Result<DataResponse<SignInResponse>> {
        return authService.sign(request)
    }


}