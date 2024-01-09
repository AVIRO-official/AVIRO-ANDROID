package com.android.aviro.data.di

import com.android.aviro.BuildConfig
import com.android.aviro.data.api.AuthService
import com.android.aviro.data.api.MemberService
import com.android.aviro.data.utils.ResultCallAdapterFactory
import com.android.aviro.data.entity.auth.TokensResponseDTO
import com.android.aviro.data.utils.HeaderInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    val BASE_URL = BuildConfig.AWS_API_KEY_V1

    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC  // 로그 레벨 BASIC
    }

    val gson = GsonBuilder()
        .registerTypeAdapter(TokensResponseDTO::class.java, SealedClassDeserializer(TokensResponseDTO::class.java))
        // 다른 sealed class에 대해서도 동일한 방식으로 등록 가능
        .create()


    @Provides
    @Singleton
    fun providesResultCallAdapterFactory(): ResultCallAdapterFactory = ResultCallAdapterFactory()

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addNetworkInterceptor(HeaderInterceptor())
            .build()

    }


    @Singleton
    @Provides
    fun provideRetrofit(client : OkHttpClient, resultCallAdapterFactory: ResultCallAdapterFactory): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //gson
            .addCallAdapterFactory(resultCallAdapterFactory)
            .build()
    }

    // 싱글톤으로 auth api 인스턴스를 생성
    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideMemberService(retrofit: Retrofit): MemberService {
        return retrofit.create(MemberService::class.java)
    }
}