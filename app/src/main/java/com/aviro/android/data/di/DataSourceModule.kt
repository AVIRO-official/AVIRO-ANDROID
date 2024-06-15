package com.aviro.android.data.di


import com.aviro.android.data.datasource.restaurant.*
import com.aviro.android.data.datasource.auth.AuthDataSource
import com.aviro.android.data.datasource.auth.AuthDataSourceImp
import com.aviro.android.data.datasource.challenge.ChallengeDataSource
import com.aviro.android.data.datasource.challenge.ChallengeDataSourceImp
import com.aviro.android.data.datasource.datastore.DataStoreDataSource
import com.aviro.android.data.datasource.datastore.DataStoreDataSourceImp
import com.aviro.android.data.datasource.marker.MarkerMemoryCacheDataSource
import com.aviro.android.data.datasource.marker.MarkerMemoryCacheDataSourceImp
import com.aviro.android.data.datasource.member.MemberDataSource
import com.aviro.android.data.datasource.member.MemberDataSourceImp

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsAuthDataSource(
        auth_datasource: AuthDataSourceImp,
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindsDataStoreDataSource(
        datastore_datasource: DataStoreDataSourceImp,
    ): DataStoreDataSource


    @Binds
    @Singleton
    abstract fun bindsMemberDataSource(
        member_datasource: MemberDataSourceImp,
    ): MemberDataSource

    @Binds
    @Singleton
    abstract fun bindsRestaurantDataSource(
        restaurant_remote_datasource: RestaurantAviroDataSourceImp,
    ): RestaurantAviroDataSource


    @Binds
    @Singleton
    abstract fun bindsRestaurantLocalDataSource(
        restaurant_local_datasource: RestaurantLocalDataSourceImp,
    ): RestaurantLocalDataSource

    @Binds
    @Singleton
    abstract fun bindsRestaurantKakaoDataSource(
        restaurant_kakao_datasource: RestaurantKakaoDataSourceImp,
    ): RestaurantKakaoDataSource

    @Binds
    @Singleton
    abstract fun bindsMarkerMemoryCacheDataSource(
        marker_memoryCache_datasource: MarkerMemoryCacheDataSourceImp,
    ): MarkerMemoryCacheDataSource

    @Binds
    @Singleton
    abstract fun bindsChallengeDataSource(
        challenge_datasource: ChallengeDataSourceImp,
    ): ChallengeDataSource

    @Binds
    @Singleton
    abstract fun bindsRestaurantPublicDataSource(
        restaurant_public_datasource: RestaurantPublicDataSourceImp,
    ): RestaurantPublicDataSource



}