package com.lee.picturenote.data

import com.lee.picturenote.data.remote.PictureApi
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.interfaces.MainRepository
import retrofit2.Response
import javax.inject.Inject

/**
 * MainRepository 구현부
 * **/
class MainRepositoryImpl @Inject constructor(
    private val pictureApi: PictureApi
) : MainRepository {
    override suspend fun getPictureList(page : Int): Response<MutableList<Picture>> {
        return pictureApi.getPictureList(page)
    }
}