package com.aviro.android.domain.usecase.member

import android.util.Log
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.APPLE
import com.aviro.android.domain.entity.key.GOOGLE
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.domain.entity.key.REFRESH_TOKEN_KEY
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.MemberRepository
import com.aviro.android.presentation.sign.GoogleSignInManager
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import javax.inject.Inject

class WithdrawUseCase  @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository : AuthRepository,
    private val memberRepository : MemberRepository,
    private val getMyInfoUseCase : GetMyInfoUseCase,
) {

    suspend operator fun invoke() : MappingResult {

        // 사용자 정보 있는지 확인하고 불러옴 -> 서버 삭제 -> 소셜 로그인 끊기/로그아웃 -> 로컬 정보 삭제
        Log.d("WithdrawUseCase","WithdrawUseCase 넘어옴")

        getMyInfoUseCase.getUserId().let { user_id ->
            Log.d("WithdrawUseCase","$user_id")

            when(user_id) {
                is MappingResult.Error -> {
                    return MappingResult.Error(null)
                }

                is MappingResult.Success<*> -> {
                    // 어떤 토큰이 저장되어 있는지 확인
                    val signType = authRepository.getSignTypeFromLocal()
                    val tokens = authRepository.getTokensFromLocal()
                    val userId = user_id.data as String
                    Log.d("WithdrawUseCase","$userId $signType")
                    Log.d("WithdrawUseCase","$signType")

                    when (signType) {
                        NAVER -> {

                            val response = memberRepository.deleteMember(userId, signType)
                            Log.d("WithdrawUseCase","$response")

                            when (response) {
                                is MappingResult.Error -> return response
                                is MappingResult.Success<*> -> {
                                    // 네이버 연결 끊기
                                    // 네이버 SDK 정보 제거
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
                                    // 로컬에서 제거
                                    authRepository.removeSignTypeFromLocal()
                                    memberRepository.removeMemberInfoFromLocal()

                                    return response
                                }

                            }

                        }

                        KAKAO -> {
                            var response = memberRepository.deleteMember(userId, signType)
                            when (response) {
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
                            return response
                        }

                        APPLE -> {
                            // remote에서 제거 // 유저 정보 제거
                            tokens[0].get(REFRESH_TOKEN_KEY)?.let { token ->
                                val response = memberRepository.deleteMember(token, signType)
                                when (response) {
                                    is MappingResult.Success<*> -> {
                                        // 로컬에서 제거
                                        authRepository.removeTokens()
                                        memberRepository.removeMemberInfoFromLocal()
                                        authRepository.removeSignTypeFromLocal()
                                    }

                                    is MappingResult.Error -> {}

                                }
                                return response

                            } ?: run {
                                return MappingResult.Error("애플 회원 탈퇴에 실패했습니다.")
                            }
                        }


                        GOOGLE -> {
                            val response = memberRepository.deleteMember(userId, signType)
                            when (response) {
                                is MappingResult.Success<*> -> {
                                    // 구글 연결 끊기
                                    GoogleSignInManager.getClient().signOut()
                                    GoogleSignInManager.getClient().revokeAccess()
                                    // 로컬 정보 삭제
                                    authRepository.removeSignTypeFromLocal()
                                    memberRepository.removeMemberInfoFromLocal()
                                }

                                is MappingResult.Error -> {}
                            }
                            return response
                        }

                       else -> {}
                    }
                }
            }
        }
        return MappingResult.Error("회원탈퇴에 실패했습니다.\n다시 시도해주세요.")
    }
}

