package com.aviro.android.domain.usecase.member

import android.util.Log
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.APPLE
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.domain.entity.key.REFRESH_TOKEN_KEY
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.MemberRepository
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import javax.inject.Inject

class WithdrawUseCas  @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository : AuthRepository,
    private val memberRepository : MemberRepository,
    private val getMyInfoUseCase : GetMyInfoUseCase,
) {

    suspend operator fun invoke() : MappingResult {

        val signType = authRepository.getSignTypeFromLocal()
        Log.d("WithdrawUseCas","${signType}")

        when(signType) {
            NAVER -> {
                val userId = getMyInfoUseCase.getUserId()
                Log.d("WithdrawUseCas","${userId}")

                userId?.let { user_id ->
                    Log.d("WithdrawUseCas","${user_id}")

                    when(user_id){

                        is MappingResult.Error -> {
                            return userId
                        }

                        is MappingResult.Success<*> -> {
                            val response = memberRepository.deleteMember(user_id.data as String, signType)
                            Log.d("WithdrawUseCas:Success","${response}")

                             when(response){
                                is MappingResult.Error ->  return response

                                is MappingResult.Success<*> -> {
                                    // 로컬에서 제거
                                    authRepository.removeSignTypeFromLocal()
                                    memberRepository.removeMemberInfoFromLocal()
                                    /*
                                    NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
                                        override fun onSuccess() { // 연결 해제, 토큰 삭제
                                        }
                                        // 연결 해제 X, 토큰 삭제
                                        override fun onFailure(httpStatus: Int, message: String) {
                                            /*result = MappingResult.Success("네이버 계정 자동 연동 해제에 실패했습니다.\n" +
                                                    "해제 하시려면 직접 네이버 계정 관리를 이용해주세요.",null)
                                             */
                                        }
                                        override fun onError(errorCode: Int, message: String) {
                                            /*result = MappingResult.Success("네이버 계정 자동 연동 해제에 실패했습니다.\n" +
                                                    "해제 하시려면 직접 네이버 계정 관리를 이용해주세요.",null)

                                             */
                                            onFailure(errorCode, message)
                                        }
                                    })

                                     */


                                    return response
                                }

                            }

                        }

                }
                }

            }

            KAKAO -> {
                val userId = getMyInfoUseCase.getUserId()
                userId?.let { user_id ->
                    when(user_id){
                        is MappingResult.Success<*> -> {
                            var result = memberRepository.deleteMember(user_id.data as String, signType)
                            when(result){
                                is MappingResult.Success<*> -> {
                                    // 연결 끊기
                                    UserApiClient.instance.unlink { error ->
                                        if (error != null) {
                                            /*
                                            result = MappingResult.Success("카카오계정 자동 연동 해제에 실패했습니다.\n" +
                                                    "해제 하시려면 직접 카카오 계정 관리를 이용해주세요.", null)
                                             */
                                        }
                                    }

                                    UserApiClient.instance.logout {}
                                    authRepository.removeSignTypeFromLocal()
                                    memberRepository.removeMemberInfoFromLocal()
                                }
                                is MappingResult.Error -> {}
                            }
                            return result
                        }
                        is MappingResult.Error -> {
                            return userId
                        }
                    }
                }
            }

            APPLE -> {
                // remote에서 제거 // 유저 정보 제거
                val refresh_token = authRepository.getTokensFromLocal()[0].get(REFRESH_TOKEN_KEY)

                refresh_token?.let {
                    val result = memberRepository.deleteMember(refresh_token, signType)
                    when(result){
                        is MappingResult.Success<*> -> {
                            // 로컬에서 제거
                            authRepository.removeTokens()
                            memberRepository.removeMemberInfoFromLocal()
                        }
                        is MappingResult.Error -> {}

                    }
                    return result
                }
            }
        }

       return MappingResult.Error("회원탈퇴에 실패했습니다.\n다시 시도해주세요.")
    }
}