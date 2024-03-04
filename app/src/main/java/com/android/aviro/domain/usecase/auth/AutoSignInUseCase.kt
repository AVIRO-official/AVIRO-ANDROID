package com.android.aviro.domain.usecase.auth

import com.android.aviro.data.model.auth.SignInResponse
import com.android.aviro.domain.entity.auth.Sign
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject

class AutoSignInUseCase @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository: AuthRepository,
    private val memberRepository : MemberRepository,
) {

    // 자동로그인
    suspend operator fun invoke(token: String): MappingResult { //Result<DataBodyResponse<SignResponseDTO>> { //DataBodyResponse<SignResponseDTO> 보내거나 BaseRespomse
        val response = authRepository.requestSignIn(token)
        when(response){
            is MappingResult.Success<*> -> {
                response.let {
                    val data = it.data as Sign
                    // 로그인 성공시 유저 정보를 저장
                    memberRepository.saveMemberInfoToLocal("user_id",data.userId)
                    data.userName
                    data.userName?.let { it1 -> memberRepository.saveMemberInfoToLocal("user_name", it1) }
                    memberRepository.saveMemberInfoToLocal("user_email",data.userEmail)
                    memberRepository.saveMemberInfoToLocal("nickname",data.nickname)
                }
            }
            is MappingResult.Error -> {}
        }
        return response

        /*
        response.onSuccess {
            val code = it.statusCode
            val data = it.data
            // 자동 로그인 성공 -> 사용자 정보 저장
            if (code == 200 && data != null) {
                memberRepository.saveMemberInfoToLocal(
                    user_id = data.userId,
                    user_name = data.userName,
                    user_email = data.userEmail,
                    nickname = data.nickname
                )
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

         */

    }

}

