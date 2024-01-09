package com.android.aviro.data.repository

import com.android.aviro.data.datasource.auth.AuthDataSource
import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.entity.auth.SignRequestDTO
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject



class AuthRepositoryImp @Inject constructor(
    private val authDataSource : AuthDataSource,
    private val dataStoreDataSource : DataStoreDataSource,
    private val memberRepository : MemberRepository,

) : AuthRepository {

    override suspend fun getTokensFromLocal() : Boolean {
        // 로컬에서 찾기
        val refresh_token = dataStoreDataSource.readDataStore("refresh_token")
        if(refresh_token != "") return false

        // 서버에서 자동 로그인 요청하기
        return requestSignIn(refresh_token)
    }

    override suspend fun requestSignIn(token : String) : Boolean {
        val respoense = authDataSource.sign(SignRequestDTO(token))
        respoense.onSuccess {
            when(it) {
                is SignResponseDTO -> {
                    // 자동로그인 성공
                    memberRepository.saveMemberInfoToLocal(user_id = it.userId, user_name = it.userName, user_email = it.userEmail, nickname = it.nickname)
                    return true
                }
                is BaseResponse -> return false
            }
        }
        return false
    }

    override suspend fun getTokensFromRemote(id_token : String, auth_code : String) : Result<Any> {
        // 성공시 -> 토큰 저장, (회원 기본 정보 저장), 회원여부 반환
        val request = TokensRequestDTO(id_token, auth_code)
        return Result.success(authDataSource.getTokens(request = request))

    }


    override suspend fun saveTokenToLocal(access_token : String, refresh_token : String) {
        // Token 받아 datastore에 저장
        dataStoreDataSource.writeDataStore("access_token", access_token)
        dataStoreDataSource.writeDataStore("refresh_token",refresh_token)

    }


    override suspend fun removeTokens() {
        // remote 제거
        authDataSource.removeTokens(dataStoreDataSource.readDataStore("refresh_token")!!)
        // local 제거
        dataStoreDataSource.removeDataStore("access_token")
        dataStoreDataSource.removeDataStore("refresh_token")

    }

}