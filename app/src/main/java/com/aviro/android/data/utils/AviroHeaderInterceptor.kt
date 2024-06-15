package com.aviro.android.data.utils

import okhttp3.Interceptor
import okhttp3.Response

class AviroHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response { //retrofit2, okhttp3
        val builder = chain.request().newBuilder()
        builder.addHeader("X-API-KEY", "${com.aviro.android.BuildConfig.AWS_API_HEADER}") //_DEBUG

        return chain.proceed(builder.build())
    }

}