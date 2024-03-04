package com.android.aviro.data.api

import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.auth.TokenResponse
import com.android.aviro.data.model.auth.TokensRequest
import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.auth.SignInRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("member")
    suspend fun getTokens(
        @Body request: TokensRequest
    ): Result<DataResponse<TokenResponse>>

    @POST("member/apple")
    suspend fun sign(
        @Body request: SignInRequest
    ): Result<DataResponse<SignInResponse>>



}
