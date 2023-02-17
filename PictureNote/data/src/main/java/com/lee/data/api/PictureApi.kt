package com.lee.data.api

import com.lee.data.common.DataUtils.Companion.ID
import com.lee.data.common.DataUtils.Companion.PAGE
import com.lee.data.common.DataUtils.Companion.PICTURE_LIST_URL
import com.lee.data.common.DataUtils.Companion.PICTURE_SEARCH_ID_URL
import com.lee.data.model.remote.PictureDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Rest Interface
 * **/
interface PictureApi {
    @GET(PICTURE_LIST_URL)
    suspend fun getPictureList(
        @Query(PAGE) page : Int
    ) : MutableList<PictureDTO>

    @GET(PICTURE_SEARCH_ID_URL)
    suspend fun getPictureById(
        @Path(ID) id : String
    ) : PictureDTO
}