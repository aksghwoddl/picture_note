package com.lee.data.repository

import com.lee.data.datasource.local.LocalDataSource
import com.lee.data.datasource.remote.RemoteDataSource
import com.lee.data.mapper.DataMapper
import com.lee.domain.model.local.FavoritePicture
import com.lee.domain.model.remote.Picture
import com.lee.domain.repository.MainRepository
import javax.inject.Inject

/**
 * MainRepository 구현부
 * **/
class MainRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MainRepository {
    /**
     * Remote Repository
     * **/
    override suspend fun getPictureList(page : Int): MutableList<Picture> {
        return DataMapper.mapperToPictureList(remoteDataSource.getPictureList(page))
    }

    override suspend fun getPictureById(id: String): Picture {
        return DataMapper.mapperToPicture(remoteDataSource.getPictureById(id))
    }

    /**
     * Local Repository
     * **/
    override suspend fun addFavoritePicture(favoritePicture : FavoritePicture) = localDataSource.addFavoritePicture(favoritePicture)

    override suspend fun getFavoritePicture() = localDataSource.getFavoritePicture()

    override suspend fun deleteFavoritePicture(favoritePicture: FavoritePicture) = localDataSource.deleteFavoritePicture(favoritePicture)

    override suspend fun getFavoritePictureCount() = localDataSource.getFavoritePictureCount()

    override suspend fun getIndexById(id: String) = localDataSource.getIndexById(id)

    override suspend fun getFavoritePictureById(id: String) = localDataSource.getFavoritePictureById(id)

    override suspend fun updateFavoritePicture(favoritePicture : FavoritePicture) = localDataSource.updateFavoritePicture(favoritePicture)
}