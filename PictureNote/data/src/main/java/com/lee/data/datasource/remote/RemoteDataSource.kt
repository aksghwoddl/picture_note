package com.lee.data.datasource.remote

import com.lee.data.model.remote.PictureDTO

interface RemoteDataSource {
    /**
     * 그림 목록 불러오기
     * **/
    suspend fun getPictureList(page : Int): MutableList<PictureDTO>

    /**
     * ID정보를 통해 그림 구하기
     * **/
    suspend fun getPictureById(id: String): PictureDTO
}