package com.android.aviro.domain.usecase.auth


import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.entity.base.MappingResult
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
    ): MappingResult { //Result<DataBodyResponse<TokensResponseDTO>>
        val response = authRepository.getTokensFromRemote(id_token, auth_code)

        when(response){
            is MappingResult.Success<*> -> {
                response.let {
                    val data = it.data as TokensResponseDTO
                    authRepository.saveTokenToLocal(data.accessToken, data.refreshToken)
                    memberRepository.saveMemberInfoToLocal("user_id", data.userId)
                }
            }
            is MappingResult.Error -> {}
        }
        return response


    }
}
