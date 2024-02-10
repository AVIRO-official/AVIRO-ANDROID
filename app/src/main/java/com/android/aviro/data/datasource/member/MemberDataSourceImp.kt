package com.android.aviro.data.datasource.member

import com.android.aviro.data.api.MemberService
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.member.*
import com.android.aviro.data.entity.member.MemberEntity
import javax.inject.Inject

class MemberDataSourceImp @Inject constructor (
    private val memberService : MemberService
): MemberDataSource {


    suspend override fun checkNickname(nickname : NicknameEntity) : Result<NicknameCheckResponse> {
        return memberService.checkNickname(nickname)
    }

    suspend override fun creatMember(newMember: MemberEntity): Result<DataBodyResponse<SignResponseDTO>> {
        return memberService.createMember(newMember)
        }


    suspend override fun getActivityCount(request : String) : Result<DataBodyResponse<MyInfoCountResponse>> {
        return memberService.getCount(request)

    }

    suspend override fun getChallengeLevel(request : String) : Result<MyInfoLevelResponse> {
        return memberService.getChallengeLevel(request)

    }

    suspend override fun deleteMember(refresh_token : String) : Result<BaseResponse> {
        return memberService.deleteUser(refresh_token)

    }

    suspend override fun updateNickname(request : NicknameUpdateRequest) : Result<BaseResponse> {
        return memberService.updateNickname(request)
    }


    }
