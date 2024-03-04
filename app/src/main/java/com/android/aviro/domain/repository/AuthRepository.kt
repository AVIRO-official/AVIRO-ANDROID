package com.android.aviro.domain.repository

import com.android.aviro.domain.entity.base.MappingResult

/** 인증과 관련된 데이터를 이 레포지토리에서 모아 처리합니다.
 *  필요시 응답 결과로 받은 DTO를 유저가 실제로 사용할 DTO로 변화합니다.
 *  Token 사용해 인증하는 과정에서 유저 정보를 응답으로 받거나 하는 경우가 있습니다.
 *  AuthRepository에서 MemberRepository를 사용해 유저 정보를 저장하는 경우도 있습니다.
 * */
interface AuthRepository {

    suspend fun getTokensFromLocal() : List<Map<String, String?>>
    suspend fun getTokensFromRemote(id_token : String, auth_code : String) : MappingResult //Result<DataBodyResponse<TokensResponseDTO>>
    suspend fun saveTokenToLocal(access_token : String, refresh_token : String)
    suspend fun removeTokens()
    suspend fun requestSignIn(token : String) : MappingResult //Result<DataBodyResponse<SignResponseDTO>>

}