package com.aviro.android

import android.content.Context
import com.aviro.android.presentation.entity.SocialType

object AuthFactory {

    private var socialAuth: AuthManager? = null
    fun getSocialLogin(context : Context, type: SocialType): AuthManager {
        if(socialAuth == null) {
            return when (type) {
                SocialType.KAKAO -> AuthKakao(context)
                SocialType.NAVER -> TODO()
                SocialType.GOOGLE -> TODO()
                SocialType.APPLE -> TODO()
            }
        }
        return socialAuth!!

    }

    fun clearSocialAuthObject() {
        socialAuth = null
    }

}

