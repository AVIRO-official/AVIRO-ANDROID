package com.android.aviro.data.di

import com.android.aviro.BuildConfig
import com.android.aviro.data.api.*
import com.android.aviro.data.utils.ResultCallAdapterFactory
import com.android.aviro.data.utils.AviroHeaderInterceptor
import com.android.aviro.data.utils.KakaoHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    private val AVIRO_BASE_URL = BuildConfig.AWS_API_KEY_V1
    private val KAKAO_BASE_URL = BuildConfig.KAKAO_RESTAPI_URL

    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC  // 로그 레벨 BASIC
    }

    @Provides
    @Singleton
    fun providesResultCallAdapterFactory(): ResultCallAdapterFactory = ResultCallAdapterFactory()


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AviroClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AviroRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoRetrofit

    @Singleton
    @Provides
    @AviroClient
    fun provideAviroClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addNetworkInterceptor(AviroHeaderInterceptor()) //aws, kakao 다르게 설정
            .build()
    }

    @Singleton
    @Provides
    @KakaoClient
    fun provideKakaoClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addNetworkInterceptor(KakaoHeaderInterceptor()) //aws, kakao 다르게 설정
            .build()
    }


    @Singleton
    @Provides
    @AviroRetrofit
    fun provideAviroRetrofit(@ApiModule.AviroClient client : OkHttpClient, resultCallAdapterFactory: ResultCallAdapterFactory): Retrofit { //AWS
        return Retrofit.Builder()
            .client(client)
            .baseUrl(AVIRO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //gson
            .addCallAdapterFactory(resultCallAdapterFactory)
            .build()
    }

    @Singleton
    @Provides
    @KakaoRetrofit
    fun provideKakaoRetrofit(@ApiModule.KakaoClient client : OkHttpClient, resultCallAdapterFactory: ResultCallAdapterFactory): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(KAKAO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //gson
            .addCallAdapterFactory(resultCallAdapterFactory)
            .build()
    }



    // 싱글톤으로 auth api 인스턴스를 생성
    @Singleton
    @Provides
    fun provideAuthService(@ApiModule.AviroRetrofit aviroRetrofit: Retrofit): AuthService {
        return aviroRetrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideMemberService(@ApiModule.AviroRetrofit aviroRetrofit: Retrofit): MemberService {
        return aviroRetrofit.create(MemberService::class.java)
    }

    @Singleton
    @Provides
    fun provideChallengeService(@ApiModule.AviroRetrofit aviroRetrofit: Retrofit): ChallengeService {
        return aviroRetrofit.create(ChallengeService::class.java)
    }


    @Singleton
    @Provides
    fun provideRestaurantService(@ApiModule.AviroRetrofit aviroRetrofit: Retrofit): RestaurantService {
        return aviroRetrofit.create(RestaurantService::class.java)
    }

    @Singleton
    @Provides
    fun provideKakaoService(@ApiModule.KakaoRetrofit kakaoRetrofit : Retrofit): KakaoService {
        return kakaoRetrofit.create(KakaoService::class.java)
    }


}