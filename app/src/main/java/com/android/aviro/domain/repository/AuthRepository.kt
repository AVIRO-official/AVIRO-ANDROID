package com.android.aviro.domain.repository

import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.base.MappingResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getTokensFromLocal() : String?// 토큰 생성 및 저장
    suspend fun getTokensFromRemote(id_token : String, auth_code : String) : MappingResult //Result<DataBodyResponse<TokensResponseDTO>>
    suspend fun saveTokenToLocal(access_token : String, refresh_token : String)
    suspend fun requestSignIn(token : String) : MappingResult //Result<DataBodyResponse<SignResponseDTO>>
    suspend fun removeTokens()

}