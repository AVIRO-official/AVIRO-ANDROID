package com.android.aviro.data.datasource.member

import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.base.BaseResponse
import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.member.*
import com.android.aviro.data.model.member.SignUpRequest

interface MemberDataSource {

    suspend fun checkNickname(nickname : String) :  Result<NicknameCheckResponse>
    suspend fun creatMember(newMember : SignUpRequest) : Result<DataResponse<SignInResponse>> // Result<Any>
    suspend fun getActivityCount(request : String) : Result<DataResponse<MemberHistoryResponse>>
    suspend fun getChallengeLevel(request : String) : Result<DataResponse<MemberLevelResponse>>
    suspend fun deleteMember(refresh_token : String) : Result<BaseResponse>
    suspend fun updateNickname(request : NicknameUpdateRequest) : Result<BaseResponse>
}