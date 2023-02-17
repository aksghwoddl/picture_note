package com.lee.data.datasource.local

import com.lee.data.model.local.dao.PictureDAO
import com.lee.domain.model.local.entity.FavoritePicture
import com.lee.domain.model.local.entity.PictureEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val pictureDAO: PictureDAO
    ) : LocalDataSource {
    /**
     * Room에 즐겨찾기 저장하기
     * **/
    override suspend fun addFavoritePicture(pictureEntity: PictureEntity) {
        pictureDAO.addFavoritePicture(pictureEntity)
    }

    /**
     * Room으로부터 즐겨찾기한 그림 목록 불러오기
     * **/
    override suspend fun getFavoritePicture(): MutableList<FavoritePicture> {
        return pictureDAO.getFavoritePicture()
    }

    /**
     * Room으로부터 즐겨찾기 삭제하기
     * **/
    override suspend fun deleteFavoritePicture(pictureEntity: PictureEntity) {
        pictureDAO.deleteFavoritePicture(pictureEntity)
    }

    /**
     * Room에 저장된 즐겨찾기 갯수 가져오기
     * **/
    override suspend fun getFavoritePictureCount(): Int {
        return pictureDAO.getFavoritePictureCount()
    }

    /**
     * Room으로부터 ID에 따른 즐겨찾기 index 가져오기
     * **/
    override suspend fun getIndexById(id: String): Int {
        return pictureDAO.getIndexById(id)
    }

    /**
     * Room으로부터 전달된 ID에 따른 즐겨찾기 가져오기
     * **/
    override suspend fun getFavoritePictureById(id: String): FavoritePicture? {
        return pictureDAO.getFavoritePictureById(id)
    }

    /**
     * Room에 저장된 즐겨찾기 정보 업데이트 하기 (Index 변경을 위해)
     * **/
    override suspend fun updateFavoritePicture(pictureEntity: PictureEntity) {
        pictureDAO.updatePicture(pictureEntity)
    }
}