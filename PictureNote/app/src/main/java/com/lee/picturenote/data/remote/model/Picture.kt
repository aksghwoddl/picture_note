package com.lee.picturenote.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * 그림 정보 class
 * **/
data class Picture(
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
)