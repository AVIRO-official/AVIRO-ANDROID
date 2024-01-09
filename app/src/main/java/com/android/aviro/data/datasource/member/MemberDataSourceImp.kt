package com.android.aviro.data.datasource.member

import com.android.aviro.data.api.MemberService
import com.android.aviro.data.entity.auth.SignResponseDTO
import com.android.aviro.data.entity.base.BaseResponse
import com.android.aviro.data.entity.base.DataBodyResponse
import com.android.aviro.data.entity.member.NicknameEntity
import com.android.aviro.data.entity.member.NicknameCheckResponse
import com.android.aviro.domain.entity.MemberEntity
import javax.inject.Inject

class MemberDataSourceImp @Inject constructor (
    private val memberService : MemberService
): MemberDataSource {


    suspend override fun checkNickname(nickname : NicknameEntity) : Result<NicknameCheckResponse> {
        return memberService.checkNickname(nickname)
    }

    suspend override fun creatMember(newMember: MemberEntity): Result<Any> {
        val response = memberService.createMember(newMember)
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
    }
