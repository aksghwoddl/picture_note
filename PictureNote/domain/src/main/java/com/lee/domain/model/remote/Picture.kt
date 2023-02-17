package com.lee.domain.model.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 그림 정보 class
 * **/
data class Picture(
    val id : String = "" ,
    val author :  String = "" ,
    val width : Int ,
    val height : Int ,
    val downloadUrl : String = "" ,
    var isFavorite : Boolean = false
) : Serializable