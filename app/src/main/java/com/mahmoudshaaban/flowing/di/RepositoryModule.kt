package com.mahmoudshaaban.flowing.di

import com.mahmoudshaaban.flowing.api.UnsplashApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        service: UnsplashApiService ,


    ): RepositoryModule{
        return provideMainRepository(
            service
        )
    }
}

