package com.lee.picturenote.data.repository

import com.lee.picturenote.data.local.dao.PictureDAO
import com.lee.picturenote.data.local.entity.PictureEntity
import com.lee.picturenote.data.remote.PictureApi
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.interfaces.MainRepository
import retrofit2.Response
import javax.inject.Inject

/**
 * MainRepository 구현부
 * **/
class MainRepositoryImpl @Inject constructor(
    private val pictureApi: PictureApi ,
    private val pictureDAO : PictureDAO
) : MainRepository {
    /**
     * Remote Repository
     * **/
    override suspend fun getPictureList(page : Int): Response<MutableList<Picture>> {
        return pictureApi.getPictureList(page)
    }

    override suspend fun getPictureById(id: String): Response<Picture> {
        return pictureApi.getPictureById(id)
    }

    /**
     * Local Repository
     * **/
    override suspend fun addFavoritePicture(pictureEntity: PictureEntity) = pictureDAO.addFavoritePicture(pictureEntity)

    override suspend fun getFavoritePicture() = pictureDAO.getFavoritePicture()

    override suspend fun deleteFavoritePicture(pictureEntity: PictureEntity) = pictureDAO.deleteFavoritePicture(pictureEntity)

    override suspend fun getFavoritePictureCount() = pictureDAO.getFavoritePictureCount()

    override suspend fun getIndexById(id: String) = pictureDAO.getIndexById(id)

    override suspend fun getFavoritePictureById(id: String) = pictureDAO.getFavoritePictureById(id)

    override suspend fun updateFavoritePicture(pictureEntity: PictureEntity) = pictureDAO.updatePicture(pictureEntity)
}