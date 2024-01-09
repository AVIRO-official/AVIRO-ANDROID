package com.android.aviro.data.di

import com.android.aviro.data.datasource.auth.AuthDataSource
import com.android.aviro.data.datasource.auth.AuthDataSourceImp
import com.android.aviro.data.datasource.datastore.DataStoreDataSource
import com.android.aviro.data.datasource.datastore.DataStoreDataSourceImp
import com.android.aviro.data.datasource.member.MemberDataSource
import com.android.aviro.data.datasource.member.MemberDataSourceImp
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


}