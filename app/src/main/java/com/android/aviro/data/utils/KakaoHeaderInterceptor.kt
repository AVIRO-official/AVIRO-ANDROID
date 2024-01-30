package com.android.aviro.data.utils

import com.android.aviro.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class KakaoHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response { //retrofit2, okhttp3
        val builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_RESTAPI_KEY}")

        return chain.proceed(builder.build())
    }
}