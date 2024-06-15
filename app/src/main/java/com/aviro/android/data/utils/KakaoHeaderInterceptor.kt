package com.aviro.android.data.utils

import okhttp3.Interceptor
import okhttp3.Response

class KakaoHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response { //retrofit2, okhttp3
        val builder = chain.request().newBuilder()
        builder.addHeader("Authorization", "KakaoAK ${com.aviro.android.BuildConfig.KAKAO_RESTAPI_KEY}")

        return chain.proceed(builder.build())
    }
}