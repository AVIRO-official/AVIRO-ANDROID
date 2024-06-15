package com.aviro.android.domain.usecase.auth

import com.aviro.android.domain.entity.key.APPLE
import com.aviro.android.domain.entity.key.KAKAO
import com.aviro.android.domain.entity.key.NAVER
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.MemberRepository
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import javax.inject.Inject

class LogoutUseCase @Inject constructor ( // 사용자에게는 소셜 로그인의 기능만 제공하면 됨
    private val authRepository : AuthRepository,
    private val memberRepository : MemberRepository
)  {
    // 로그아웃
    suspend operator fun invoke()  {
        // 로컬에서만 제거
        val signType = authRepository.getSignTypeFromLocal()

        when(signType) {
            NAVER -> NaverIdLoginSDK.logout()
            APPLE -> authRepository.removeTokens()
            KAKAO -> UserApiClient.instance.logout {}
        }

        authRepository.removeSignTypeFromLocal()
        memberRepository.removeMemberInfoFromLocal()

    }
}