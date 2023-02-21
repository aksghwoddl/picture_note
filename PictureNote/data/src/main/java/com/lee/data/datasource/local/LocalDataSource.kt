package com.lee.data.datasource.local

import com.lee.domain.model.local.FavoritePicture

interface LocalDataSource {
    /**
     * Room에 즐겨찾기 저장하기
     * **/
    suspend fun addFavoritePicture(favoritePicture : FavoritePicture)

    /**
     * Room으로부터 즐겨찾기한 그림 목록 불러오기
     * **/
    suspend fun getFavoritePicture() : MutableList<FavoritePicture>

    /**
     * Room으로부터 즐겨찾기 삭제하기
     * **/
    suspend fun deleteFavoritePicture(favoritePicture : FavoritePicture)

    /**
     * Room에 저장된 즐겨찾기 갯수 가져오기
     * **/
    suspend fun getFavoritePictureCount() : Int

    /**
     * Room으로부터 ID에 따른 즐겨찾기 index 가져오기
     * **/
    suspend fun getIndexById(id: String) : Int

    /**
     * Room으로부터 전달된 ID에 따른 즐겨찾기 가져오기
     * **/
    suspend fun getFavoritePictureById(id : String) : FavoritePicture?

    /**
     * Room에 저장된 즐겨찾기 정보 업데이트 하기 (Index 변경을 위해)
     * **/
    suspend fun updateFavoritePicture(favoritePicture: FavoritePicture)
}
