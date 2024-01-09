package com.android.aviro.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getTokensFromLocal() : Boolean// 토큰 생성 및 저장
    suspend fun getTokensFromRemote(id_token : String, auth_code : String) : Result<Any>
    suspend fun saveTokenToLocal(access_token : String, refresh_token : String)
    suspend fun requestSignIn(token : String) : Boolean
    suspend fun removeTokens()

}