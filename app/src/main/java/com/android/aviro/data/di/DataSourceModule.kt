package com.android.aviro.data.di

import com.android.aviro.data.datasource.AuthDataSource
import com.android.aviro.data.datasource.AuthDataSourceImp
import com.android.aviro.data.repository.AuthRepositoryImp
import com.android.aviro.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteModule {

        @Binds
        @Singleton
        abstract fun bindsAuthDataSource(
            repository: AuthDataSourceImp,
        ): AuthDataSource
}