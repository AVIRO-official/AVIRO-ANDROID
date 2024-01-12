package com.android.aviro.data.di

import com.android.aviro.data.datasource.auth.AuthDataSource
import com.android.aviro.data.datasource.auth.AuthDataSourceImp
import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.datasource.datastore.DataStoreDataSourceImp
import com.android.aviro.data.datasource.marker.MarkerCacheDataSource
import com.android.aviro.data.datasource.marker.MarkerCacheDataSourceImp
import com.android.aviro.data.datasource.member.MemberDataSource
import com.android.aviro.data.datasource.member.MemberDataSourceImp
import com.android.aviro.data.datasource.restaurant.RestaurantDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantDataSourceImp
import com.android.aviro.data.datasource.restaurant.RestaurantLocalDataSource
import com.android.aviro.data.datasource.restaurant.RestaurantLocalDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.Provides
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
        restaurant_remote_datasource: RestaurantDataSourceImp,
    ): RestaurantDataSource


    @Binds
    @Singleton
    abstract fun bindsRestaurantLocalDataSource(
        restaurant_local_datasource: RestaurantLocalDataSourceImp,
    ): RestaurantLocalDataSource

    @Binds
    @Singleton
    abstract fun bindsMarkerCacheDataSource(
        marker_cache_datasource: MarkerCacheDataSourceImp,
    ): MarkerCacheDataSource



}