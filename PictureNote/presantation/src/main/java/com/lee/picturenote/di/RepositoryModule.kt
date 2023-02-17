package com.lee.picturenote.di

import com.lee.picturenote.data.local.dao.PictureDAO
import com.lee.picturenote.data.remote.PictureApi
import com.lee.picturenote.data.repository.MainRepositoryImpl
import com.lee.picturenote.interfaces.MainRepository
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
    fun provideRepository(api : PictureApi , pictureDAO: PictureDAO) : MainRepository {
        return MainRepositoryImpl(api , pictureDAO)
    }
}