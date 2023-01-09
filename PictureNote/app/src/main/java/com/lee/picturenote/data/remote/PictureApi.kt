package com.lee.picturenote.data.remote

import com.lee.picturenote.common.ID
import com.lee.picturenote.common.PAGE
import com.lee.picturenote.common.PICTURE_LIST_URL
import com.lee.picturenote.common.PICTURE_SEARCH_ID_URL
import com.lee.picturenote.data.remote.model.Picture
import retrofit2.Response
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
    ) : Response<MutableList<Picture>>

    @GET(PICTURE_SEARCH_ID_URL)
    suspend fun getPictureById(
        @Path(ID) id : String
    ) : Response<Picture>
}