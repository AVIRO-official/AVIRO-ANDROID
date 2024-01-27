package com.android.aviro.data.repository

import android.util.Log
import com.android.aviro.data.datasource.auth.AuthDataSource
import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.entity.auth.SignRequestDTO
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.base.MappingResult
import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject



class AuthRepositoryImp @Inject constructor(
    private val authDataSource : AuthDataSource,
    private val dataStoreDataSource : DataStoreDataSource
) : AuthRepository {

    // MappingResult 형식으로 맵핑해줘 오류인지 정상인지 판단
    override suspend fun requestSignIn(token: String): MappingResult { //Result<DataBodyResponse<SignResponseDTO>> { //DataBodyResponse<SignResponseDTO> 보내거나 BaseRespomse
        //val token = authRepository.getTokensFromLocal()
        val response = authDataSource.requestSignIn(SignRequestDTO(token))
        lateinit var  result : MappingResult

        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            // 자동 로그인 성공 -> 사용자 정보 저장
            if (code == 200 && data != null) {
                result = MappingResult.Success(it.statusCode, it.message, it.data)
            } else {
                // 서버 내부 에러
                when(code){
                    400 -> result = MappingResult.Error(it.statusCode, it.message ?: "잘못된 요청값 입니다.\\n다시 로그인 해주세요")
                    500 -> result = MappingResult.Error(it.statusCode, it.message ?: "서버 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.statusCode, it.message ?: "알 수 없는 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                }

            }
        }.onFailure {
            // 통신 에러
            result =  MappingResult.Error(0, it.message)
        }

        return result

    }

    override suspend fun getTokensFromLocal() : String? {
        // 로컬에서 찾기
        return dataStoreDataSource.readDataStore("refresh_token")
    }

    // 서버로부터 access, refrsh token, 회원여부 요청
    override suspend fun getTokensFromRemote(id_token : String, auth_code : String) : MappingResult { //Result<DataBodyResponse<TokensResponseDTO>>
        val response = authDataSource.getTokens(request = TokensRequestDTO(id_token, auth_code))

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            // 자동 로그인 성공 -> 사용자 정보 저장
            if (code == 200 && data != null) {
                result = MappingResult.Success(it.statusCode, it.message, it.data)
            } else {
                // 서버 내부 에러
                when(code){
                    400 -> result = MappingResult.Error(it.statusCode, it.message ?: "잘못된 요청값 입니다.\\n다시 로그인 해주세요")
                    500 -> result = MappingResult.Error(it.statusCode, it.message ?: "서버 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.statusCode, it.message ?: "알 수 없는 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                }

            }
        }.onFailure {
            // 통신 에러
            result =  MappingResult.Error(0, it.message)
        }

        return result

        //return response

    }

    override suspend fun saveTokenToLocal(access_token : String, refresh_token : String) {
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