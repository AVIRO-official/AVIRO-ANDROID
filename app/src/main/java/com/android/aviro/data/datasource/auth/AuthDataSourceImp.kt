package com.android.aviro.data.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.aviro.data.api.AuthService
import com.android.aviro.data.entity.auth.AuthRequestDTO
import com.android.aviro.data.entity.auth.AuthResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AuthRemoteDataSourceImp @Inject constructor(
    private val authService : AuthService
) : AuthDataSource  {


    override suspend fun getTokens(request: AuthRequestDTO) : Result<Any> {
        val response = authService.getTokens(request)

        response.onSuccess {
            val data = it.data
            Log.d("data", "${data}")
            if (data != null) {
                return Result.success(it.data)
            } else {
                val errorResponse = BaseResponse(it.statusCode, it.message.orEmpty())
                return Result.success(errorResponse)
            }

        }.onFailure {

        }
        return Result.failure(IllegalStateException("알 수 없는 오류가 발생했습니다."))
    }


    override suspend fun removeTokens() {

    }

    override suspend fun getTokens() : String {
        throw UnsupportedOperationException("getTokens() is not supported in AuthRemoteDataSource")
    }

}