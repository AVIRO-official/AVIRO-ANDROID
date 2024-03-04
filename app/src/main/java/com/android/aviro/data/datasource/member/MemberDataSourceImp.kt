package com.android.aviro.data.datasource.member

import com.android.aviro.data.api.MemberService
import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.data.model.base.BaseResponse
import com.android.aviro.data.model.base.DataResponse
import com.android.aviro.data.model.member.*
import com.android.aviro.data.model.member.SignUpRequest
import javax.inject.Inject

class MemberDataSourceImp @Inject constructor (
    private val memberService : MemberService
): MemberDataSource {


    suspend override fun checkNickname(nickname : String) : Result<NicknameCheckResponse> {
        return memberService.checkNickname(nickname)
    }

    suspend override fun creatMember(newMember: SignUpRequest): Result<DataResponse<SignInResponse>> {
        return memberService.createMember(newMember)
        }


    suspend override fun getActivityCount(request : String) : Result<DataResponse<MemberHistoryResponse>> {
        return memberService.getCount(request)

    }

    suspend override fun getChallengeLevel(request : String) : Result<DataResponse<MemberLevelResponse>> {
        return memberService.getChallengeLevel(request)

    }

    suspend override fun deleteMember(refresh_token : String) : Result<BaseResponse> {
        return memberService.deleteUser(refresh_token)

    }

    suspend override fun updateNickname(request : NicknameUpdateRequest) : Result<BaseResponse> {
        return memberService.updateNickname(request)
    }


    }
