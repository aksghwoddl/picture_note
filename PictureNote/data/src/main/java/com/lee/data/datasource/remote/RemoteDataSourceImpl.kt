package com.lee.data.datasource.remote

import com.lee.data.api.PictureApi
import com.lee.data.model.remote.PictureDTO
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val pictureApi: PictureApi
) : RemoteDataSource {
    /**
     * 그림 목록 불러오기
     * **/
    override suspend fun getPictureList(page: Int): MutableList<PictureDTO> {
        return pictureApi.getPictureList(page)
    }

    /**
     * ID정보를 통해 그림 구하기
     * **/
    override suspend fun getPictureById(id: String): PictureDTO {
        return pictureApi.getPictureById(id)
    }

}