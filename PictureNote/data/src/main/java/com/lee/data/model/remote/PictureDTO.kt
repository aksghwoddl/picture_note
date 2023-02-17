package com.lee.data.model.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 그림 정보 class
 * **/
data class PictureDTO(
    @Expose
    val id : String = "" ,
    @Expose
    val author :  String = "" ,
    @Expose
    val width : Int ,
    @Expose
    val height : Int ,
    @Expose
    val url : String = "" ,
    @SerializedName("download_url")
    @Expose
    val downloadUrl : String = "" ,
    var isFavorite : Boolean = false
) : Serializable