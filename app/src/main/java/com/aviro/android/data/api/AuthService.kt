package com.aviro.android.data.api


import com.aviro.android.data.model.auth.SignInRequest
import com.aviro.android.data.model.auth.SignInResponse
import com.aviro.android.data.model.auth.TokenResponse
import com.aviro.android.data.model.auth.TokensRequest
import com.aviro.android.data.model.auth.UserResponse
import com.aviro.android.data.model.base.DataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("member")
    suspend fun getTokens(
        @Body request: TokensRequest
    ): Result<DataResponse<TokenResponse>>

    @POST("member/apple")
    suspend fun sign(
        @Body request: SignInRequest
    ): Result<DataResponse<SignInResponse>>

    @GET("member/kakao")
    suspend fun getUser ( // 사용자의 닉네임 가져옴(400이면 회원가입 필요)
        @Query("userId") userId : String
    ): Result<DataResponse<UserResponse>>


}
