package com.android.aviro.data.di

import com.android.aviro.BuildConfig
import com.android.aviro.data.api.AuthService
import com.android.aviro.data.api.KakaoService
import com.android.aviro.data.api.MemberService
import com.android.aviro.data.api.RestaurantService
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
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    val AVIRO_BASE_URL = BuildConfig.AWS_API_KEY_V1
    val KAKAO_BASE_URL = BuildConfig.KAKAO_RESTAPI_URL

    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC  // 로그 레벨 BASIC
    }

    @Provides
    @Singleton
    fun providesResultCallAdapterFactory(): ResultCallAdapterFactory = ResultCallAdapterFactory()

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addNetworkInterceptor(HeaderInterceptor()) //aws, kakao 다르게 설정
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AviroRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KakaoRetrofit


    @Singleton
    @Provides
    @AviroRetrofit
    fun provideAviroRetrofit(client : OkHttpClient, resultCallAdapterFactory: ResultCallAdapterFactory): Retrofit { //AWS
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
    fun provideRetrofitKakao(client : OkHttpClient, resultCallAdapterFactory: ResultCallAdapterFactory): Retrofit {
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
    fun provideRestaurantService(@ApiModule.AviroRetrofit aviroRetrofit: Retrofit): RestaurantService {
        return aviroRetrofit.create(RestaurantService::class.java)
    }

    @Singleton
    @Provides
    fun provideKakaoService(@ApiModule.KakaoRetrofit kakaoRetrofit : Retrofit): KakaoService {
        return kakaoRetrofit.create(KakaoService::class.java)
    }


}