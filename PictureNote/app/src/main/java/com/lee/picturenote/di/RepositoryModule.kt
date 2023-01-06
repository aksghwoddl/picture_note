package com.lee.picturenote.di

import com.lee.picturenote.data.MainRepositoryImpl
import com.lee.picturenote.data.remote.PictureApi
import com.lee.picturenote.domain.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository 관련 DI module
 * **/
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(api : PictureApi) : MainRepository {
        return MainRepositoryImpl(api)
    }
}