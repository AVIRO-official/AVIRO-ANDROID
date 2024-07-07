package com.aviro.android

import android.content.Context
import android.util.Log

import com.aviro.android.presentation.entity.SignInfo
import com.aviro.android.presentation.entity.SocialType
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthKakao(val context : Context) : AuthManager {

    override fun initialize() {
        // Kakao SDK 초기화
        KakaoSdk.init(context, com.aviro.android.BuildConfig.KAKAO_SDK_KEY)
    }

    override suspend fun autoSignIn() : Boolean {

        if (!AuthApiClient.instance.hasToken()) {
            //로그인 필요
            Log.d("autoSignIn:KAKAO","재로그인 필요")
            return false
        } else {
            val isSignIn = checkKakaoAccessToken() // accessToken 갱신 여부
            if (isSignIn) {
                Log.d("autoSignIn:KAKAO","로그인 성공 ")
               return true
            } else {
                Log.d("autoSignIn:KAKAO","재로그인 필요")
                return false
            }
        }
    }

    suspend fun checkKakaoAccessToken(): Boolean = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.accessTokenInfo { _, error ->
            if (error != null) {
                continuation.resume(false)
            } else {
                continuation.resume(true)
            }
        }
    }


    override suspend fun manualSignIn() : SignInfo? {
        // 카카오계정으로 로그인
        return suspendCancellableCoroutine { continuation ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    continuation.resume(null)
                 /*   AviroDialogUtils.createOneDialog(
                        context,
                        "카카오 소셜로그인 에러",
                        "카카오계정으로 로그인할 수 없습니다.\n다시 시도하거나 다른 소셜로그인을 이용해주세요",
                        "확인"
                    )*/

                } else if (token != null) {
                    Log.d("manualSignIn:KAKAO", "카카오계정으로 로그인 성공")
                    // 사용자 정보 받아옴
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            // 사용자 정보 가져오지 못하면 로그인 취소
                            UserApiClient.instance.logout {}
                        } else if (user != null) {
                            val userId = user.id.toString()
                            val userName = user.kakaoAccount?.profile?.nickname
                            val email = user.kakaoAccount?.email // 이름

                            // 사용자 정보를 바로 저장할 것이 아니기 때문에 정보 담은 객체 반환
                            val signInfo =
                                SignInfo(userId, userName ?: "", email ?: "", SocialType.KAKAO)

                            continuation.resume(signInfo)
                        }
                    }
                }
            }
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)

            continuation.invokeOnCancellation {
                // 코루틴이 취소될 경우 처리할 작업
                continuation.resume(null)
            }
        }

    }

    override fun logout() {
        UserApiClient.instance.logout {}
    }

    override fun withdraw() {
        UserApiClient.instance.unlink {}
        UserApiClient.instance.logout {}
    }
}