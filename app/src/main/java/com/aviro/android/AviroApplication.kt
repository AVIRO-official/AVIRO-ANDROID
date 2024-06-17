package com.aviro.android

import android.app.Application
import androidx.core.content.ContentProviderCompat.requireContext
import com.aviro.android.presentation.sign.GoogleSignInManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AviroApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Naver SDK 초기화
        val naverClientId = com.aviro.android.BuildConfig.NAVER_LOGIN_CLIENT_ID
        val naverClientSecret =  com.aviro.android.BuildConfig.NAVER_LOGIN_CLIENT_SECRET
        val naverClientName =  "NAVER_LOGIN_SERVICE"

        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret , naverClientName)

        // Kakao SDK 초기화
        KakaoSdk.init(this, com.aviro.android.BuildConfig.KAKAO_SDK_KEY)

        // 구글 초기화
        GoogleSignInManager.initialize(this, BuildConfig.GOOGLE_CLIENT_ID)

    }

}