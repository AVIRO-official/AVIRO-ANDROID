package com.android.aviro.data.utils

import com.android.aviro.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response { //retrofit2, okhttp3
        val builder = chain.request().newBuilder()
        builder.addHeader("X-API-KEY", "${BuildConfig.AWS_API_HEADER}")

        return chain.proceed(builder.build())
    }

}