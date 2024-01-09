package com.android.aviro.data.datasource.auth

import com.android.aviro.data.api.AuthService
import com.android.aviro.data.entity.auth.SignRequestDTO
import com.android.aviro.data.entity.auth.TokensRequestDTO
import com.android.aviro.data.entity.base.BaseResponse
import javax.inject.Inject


class AuthDataSourceImp @Inject constructor(
    private val authService : AuthService
) : AuthDataSource {


    override suspend fun getTokens(request: TokensRequestDTO) : Result<Any> {
        val response =  authService.getTokens(request)
        // 타입 변형
        response.onSuccess {
            val data = it.data

            if (data != null) {
                return Result.success(it.data)
            } else {
                val errorResponse = BaseResponse(it.statusCode, it.message.orEmpty())
                return Result.success(errorResponse)
            }

        }
        return Result.failure(IllegalStateException("알 수 없는 오류가 발생했습니다."))

    }

    override suspend fun sign(request: SignRequestDTO): Result<Any> {
        val response =  authService.sign(request)
        response.onSuccess {
            val data = it.data
            if (data != null) {
                return Result.success(it.data)
            } else {
                val errorResponse = BaseResponse(it.statusCode, it.message.orEmpty())
                return Result.success(errorResponse)
            }

        }
        return Result.failure(IllegalStateException("알 수 없는 오류가 발생했습니다."))
    }


    override suspend fun removeTokens(refresh_token : String) {

    }



}