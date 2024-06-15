package com.aviro.android.domain.usecase.auth


import com.aviro.android.domain.entity.auth.Tokens
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.USER_ID_KEY
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.MemberRepository
import javax.inject.Inject



class CreateTokensUseCase  @Inject constructor (
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(
        id_token: String,
        auth_code: String
    ): MappingResult {
        val response = authRepository.getTokensFromRemote(id_token, auth_code)
        when(response){
            is MappingResult.Success<*> -> {
                response.let {
                    val data = it.data as Tokens
                    authRepository.saveTokenToLocal(data.accessToken, data.refreshToken)
                    memberRepository.saveMemberInfoToLocal(USER_ID_KEY, data.userId)
                }
            }
            is MappingResult.Error -> {}
        }
        return response

    }
}
