package com.android.aviro.data.datasource.auth

import android.util.Log
import com.android.aviro.data.api.AuthService
import com.android.aviro.data.entity.auth.SignRequestDTO
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.DataBodyResponse
import java.lang.Exception
import javax.inject.Inject


class AuthDataSourceImp @Inject constructor(
    private val authService : AuthService
) : AuthDataSource {

    // 서버로 refresh token 요청
    override suspend fun getTokens(request: TokensRequestDTO) : Result<DataBodyResponse<TokensResponseDTO>> {
        val response =  authService.getTokens(request)
        return response

    }

    // 서버로 로그인 요청
    override suspend fun requestSignIn(request: SignRequestDTO) : Result<DataBodyResponse<SignResponseDTO>> {
        //val response =  authService.sign(request)
        return authService.sign(request)
    }


}