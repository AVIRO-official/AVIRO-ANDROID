package com.android.aviro.data.datasource

import com.android.aviro.data.entity.auth.AuthRequestDTO
import com.android.aviro.data.entity.auth.AuthResponseDTO
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {

    suspend fun getTokens(request: AuthRequestDTO) : Result<Any> // local, remote로 부터 토큰 가져옴
    suspend fun getTokens() : String
    suspend fun removeTokens()
    suspend fun updateTokens(accessToken: String, refreshToken: String, userId: String)
    //fun saveToken(accessToken: String, refreshToken: String, userId: String)


}