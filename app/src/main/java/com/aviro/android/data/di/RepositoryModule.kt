package com.aviro.android.data.di


import com.aviro.android.data.repository.ChallengeRepositoryImp
import com.aviro.android.data.repository.MemberRepositoryImp
import com.aviro.android.data.repository.RestaurantRepositoryImp
import com.aviro.android.domain.repository.AuthRepository
import com.aviro.android.domain.repository.ChallengeRepository
import com.aviro.android.domain.repository.MemberRepository
import com.aviro.android.domain.repository.RestaurantRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.aviro.android.data.repository.AuthRepositoryImp as AuthRepositoryImp1


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        auth_repository: AuthRepositoryImp1,
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

    @Binds
    @Singleton
    abstract fun bindsChallengeRepository(
        challenge_repository: ChallengeRepositoryImp,
    ): ChallengeRepository





}