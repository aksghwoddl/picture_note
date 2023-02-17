package com.lee.picturenote.di

import com.lee.data.api.PictureApi
import com.lee.data.datasource.local.LocalDataSource
import com.lee.data.datasource.local.LocalDataSourceImpl
import com.lee.data.datasource.remote.RemoteDataSource
import com.lee.data.datasource.remote.RemoteDataSourceImpl
import com.lee.data.model.local.dao.PictureDAO
import com.lee.data.repository.MainRepositoryImpl
import com.lee.domain.repository.MainRepository
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
    fun provideRepository(remoteDataSource: RemoteDataSource  , localDataSource: LocalDataSource) : MainRepository {
        return MainRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(pictureDAO: PictureDAO) : LocalDataSource {
        return LocalDataSourceImpl(pictureDAO)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(pictureApi: PictureApi) : RemoteDataSource {
        return RemoteDataSourceImpl(pictureApi)
    }
}