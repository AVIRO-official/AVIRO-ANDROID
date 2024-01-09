package com.android.aviro.data.api

import com.android.aviro.data.entity.auth.SignRequestDTO
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.base.DataBodyResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("member")
    suspend fun getTokens(
        @Body request: TokensRequestDTO
    ): Result<DataBodyResponse<TokensResponseDTO>>

    @POST("member/apple")
    suspend fun sign(
        @Body request: SignRequestDTO
    ): Result<DataBodyResponse<SignResponseDTO>>



}
