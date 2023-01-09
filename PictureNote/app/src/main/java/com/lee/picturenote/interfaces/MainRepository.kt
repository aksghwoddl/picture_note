package com.lee.picturenote.interfaces

import com.lee.picturenote.data.remote.model.Picture
import retrofit2.Response

/**
 * Repository Interface
 * **/
interface MainRepository {
    suspend fun getPictureList(page : Int) : Response<MutableList<Picture>>
    suspend fun getPictureById(id : String) : Response<Picture>
}