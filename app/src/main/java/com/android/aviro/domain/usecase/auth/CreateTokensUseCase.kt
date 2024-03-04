package com.android.aviro.domain.usecase.auth


import android.util.Log
import com.android.aviro.domain.entity.auth.Tokens
import com.android.aviro.domain.entity.base.MappingResult
import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import javax.inject.Inject



class CreateTokensUseCase  @Inject constructor (
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(
        id_token: String,
        auth_code: String
    ): MappingResult {
        Log.d("id_token : ","${id_token}")
        Log.d("auth_code : ","${auth_code}")
        val response = authRepository.getTokensFromRemote(id_token, auth_code)
        Log.d("CreateTokensUseCase : ","${response}")

        when(response){
            is MappingResult.Success<*> -> {
                response.let {
                    val data = it.data as Tokens
                    authRepository.saveTokenToLocal(data.accessToken, data.refreshToken)
                    memberRepository.saveMemberInfoToLocal("user_id", data.userId)
                }
            }
            is MappingResult.Error -> {}
        }
        return response

    }
}
