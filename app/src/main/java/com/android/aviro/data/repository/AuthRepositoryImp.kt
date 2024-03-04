package com.android.aviro.data.repository

import android.graphics.Insets.add
import android.util.Log
import com.android.aviro.data.datasource.auth.AuthDataSource
import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.mapper.toSiginInRequest
import com.android.aviro.data.mapper.toSignIn
import com.android.aviro.data.mapper.toTokens
import com.android.aviro.data.mapper.toTokensRequest

import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.AuthRepository
import javax.inject.Inject


class AuthRepositoryImp @Inject constructor(
    private val authDataSource : AuthDataSource,
    private val dataStoreDataSource : DataStoreDataSource
) : AuthRepository {

    // MappingResult 형식으로 맵핑해줘 오류인지 정상인지 판단
    override suspend fun requestSignIn(token: String): MappingResult {
        val response = authDataSource.requestSignIn(toSiginInRequest(token))

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            // 자동 로그인 성공 -> 사용자 정보 저장
            if (code == 200 && data != null) {
                result = MappingResult.Success(it.message, it.data.toSignIn())
            } else {
                // 서버 내부 에러
                when(code){
                    400 -> result = MappingResult.Error(it.message ?: "잘못된 요청값 입니다.\\n다시 로그인 해주세요")
                    500 -> result = MappingResult.Error(it.message ?: "서버 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.message ?: "알 수 없는 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                }
            }
        }.onFailure {
            // 통신 에러
            result =  MappingResult.Error(it.message)
        }

        return result

    }

    // 서버로부터 access, refrsh token, 회원여부 요청
    override suspend fun getTokensFromRemote(id_token : String, auth_code : String) : MappingResult {
        val response = authDataSource.getTokens(request = toTokensRequest(id_token, auth_code))
        Log.d("getTokensFromRemote", "${response}")

        lateinit var  result : MappingResult
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            // 자동 로그인 성공 -> 사용자 정보 저장
            if (code == 200 && data != null) {
                result = MappingResult.Success(it.message, it.data.toTokens())
            } else {
                // 서버 내부 에러
                when(code){
                    400 -> result = MappingResult.Error(it.message ?: "잘못된 요청값 입니다.\\n다시 로그인 해주세요")
                    500 -> result = MappingResult.Error(it.message ?: "서버 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                    else -> result = MappingResult.Error(it.message ?: "알 수 없는 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.")
                }
            }
        }.onFailure {
            result =  MappingResult.Error(it.message)
        }
        return result

    }


    override suspend fun getTokensFromLocal() : List<Map<String, String?>> {
        // 로컬에서 찾기
        val tokens = mutableListOf<Map<String, String?>>()
        tokens.add(mapOf("refresh_token" to dataStoreDataSource.readDataStore("refresh_token"),
            "access_token" to dataStoreDataSource.readDataStore("access_token")))
        return tokens
    }

    override suspend fun saveTokenToLocal(access_token : String, refresh_token : String) {
        dataStoreDataSource.writeDataStore("access_token", access_token)
        dataStoreDataSource.writeDataStore("refresh_token",refresh_token)
    }

    override suspend fun removeTokens() {
        // local 제거
        dataStoreDataSource.removeDataStore("access_token")
        dataStoreDataSource.removeDataStore("refresh_token")

    }

}