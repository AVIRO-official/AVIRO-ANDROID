package com.aviro.android.domain.usecase.auth

import android.content.Context
import android.util.Log
import com.aviro.android.domain.entity.auth.Sign
import com.aviro.android.domain.entity.auth.User
import com.aviro.android.domain.entity.base.MappingResult
import com.aviro.android.domain.entity.key.APPLE
import com.aviro.android.domain.entity.key.GOOGLE
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.domain.entity.key.REFRESH_TOKEN_KEY
import com.aviro.android.domain.entity.key.UNKNOWN
import com.aviro.android.domain.entity.key.USER_EMAIL_KEY
import com.aviro.android.domain.entity.key.USER_ID_KEY
import com.aviro.android.domain.entity.key.USER_NAME_KEY
import com.aviro.android.domain.entity.key.USER_NICKNAME_KEY
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.MemberRepository
import com.aviro.android.domain.usecase.member.GetMyInfoUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AutoSignInUseCase @Inject constructor (
    private val authRepository: AuthRepository,
    private val memberRepository : MemberRepository,
    private val getMyInfoUseCase : GetMyInfoUseCase,
    @ApplicationContext private val context : Context,
) {

    // 자동로그인
    suspend operator fun invoke(): MappingResult {

        // SDK 로그인만 되어있을 수 있음 (실제 로컬에 정보 X)
        val userId = getMyInfoUseCase.getUserId()

        userId.let {
            when(it) {
                is MappingResult.Error -> {
                    return MappingResult.Error(null)
                }
                is MappingResult.Success<*> -> {

                    // 어떤 토큰이 저장되어 있는지 확인
                    val signType = authRepository.getSignTypeFromLocal()
                    val tokens = authRepository.getTokensFromLocal()

                    when (signType) {
                        NAVER -> {
                            val naverClientId = com.aviro.android.BuildConfig.NAVER_LOGIN_CLIENT_ID
                            val naverClientSecret =  com.aviro.android.BuildConfig.NAVER_LOGIN_CLIENT_SECRET
                            val naverClientName =  "NAVER_LOGIN_SERVICE"
                            NaverIdLoginSDK.initialize(context, naverClientId, naverClientSecret , naverClientName)

                            val state = NaverIdLoginSDK.getState().toString()
                            when (state) {
                                "OK" -> {
                                    return MappingResult.Success("", null)
                                }

                                "NEED_INIT" -> {
                                    // 초기화 필요
                                    val naverClientId = com.aviro.android.BuildConfig.NAVER_LOGIN_CLIENT_ID
                                    val naverClientSecret =  com.aviro.android.BuildConfig.NAVER_LOGIN_CLIENT_SECRET
                                    val naverClientName =  "NAVER_LOGIN_SERVICE"

                                    NaverIdLoginSDK.initialize(context, naverClientId, naverClientSecret , naverClientName)

                                    return MappingResult.Error(null)
                                }

                                "NEED_LOGIN" -> {

                                    return MappingResult.Error(null)
                                }

                                "NEED_REFRESH_TOKEN" -> { // 액세스 토큰만 만료
                                    val oauthLoginCallback = object : OAuthLoginCallback {
                                        override fun onSuccess() {
                                        }

                                        override fun onFailure(httpStatus: Int, message: String) {
                                            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                                            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                                            Log.e("test", "$errorCode $errorDescription")
                                        }

                                        override fun onError(errorCode: Int, message: String) {
                                            onFailure(errorCode, message)
                                        }
                                    }
                                    NidOAuthLogin().callRefreshAccessTokenApi(oauthLoginCallback)
                                }

                                else -> {}
                            }
                        }


                        KAKAO -> {
                            if (AuthApiClient.instance.hasToken()) {
                                val isSignIn = checkKakaoAccessToken()
                                if (isSignIn) {
                                    // 회원가입 했는지 확인
                                    Log.d("AutoSignInUseCase:KAKAO","로그인 성공 ")
                                    return MappingResult.Success("", null)
                                } else {
                                    return MappingResult.Error(null)
                                }
                            } else {
                                //로그인 필요
                                Log.d("AutoSignInUseCase:KAKAO","재로그인 필요")
                                return MappingResult.Error(null)
                            }
                        }

                        APPLE -> {
                            val refresh_token = tokens[0][REFRESH_TOKEN_KEY]!!

                            val response = authRepository.requestSignIn(refresh_token)
                            when (response) {
                                // 애플 자동로그인 성공
                                is MappingResult.Success<*> -> {
                                    response.let {
                                        val data = it.data as Sign
                                        // 로그인 성공시 유저 정보를 저장, 토큰 타입 저장
                                        memberRepository.saveMemberInfoToLocal(USER_ID_KEY, data.userId)
                                        data.userName?.let { name ->
                                            memberRepository.saveMemberInfoToLocal(USER_NAME_KEY, name)
                                        }
                                        memberRepository.saveMemberInfoToLocal(USER_EMAIL_KEY, data.userEmail)
                                        memberRepository.saveMemberInfoToLocal(USER_NICKNAME_KEY, data.nickname)
                                    }
                                }

                                is MappingResult.Error -> {}
                            }
                            return response
                        }

                        GOOGLE -> {
                            return MappingResult.Success("", null)
                        }


                        else ->  return MappingResult.Error(null)
                }
            }
        }

        }
        return MappingResult.Error(null)
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


}

