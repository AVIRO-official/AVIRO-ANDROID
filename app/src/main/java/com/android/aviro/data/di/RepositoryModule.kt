package com.android.aviro.data.di

import com.android.aviro.data.repository.AuthRepositoryImp
import com.android.aviro.data.repository.MemberRepositoryImp
import com.android.aviro.data.repository.RestaurantRepositoryImp
import com.android.aviro.domain.repository.AuthRepository
import com.android.aviro.domain.repository.MemberRepository
import com.android.aviro.domain.repository.RestaurantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        auth_repository: AuthRepositoryImp,
    ): AuthRepository


    @Binds
    @Singleton
    abstract fun bindsMemberRepository(
       member_repository: MemberRepositoryImp,
    ): MemberRepository

    @Binds
    @Singleton
    abstract fun bindsRestaurantRepository(
        restaurant_repository: RestaurantRepositoryImp,
    ): RestaurantRepository




}