package com.lee.picturenote.data.local.entity

import com.lee.picturenote.data.remote.model.Picture

/**
 * Room으로 부터 받은 즐겨찾기 목록을 담는 holder class
 * **/
data class FavoritePicture(
    val id : String ,
    val picture: Picture ,
    var index : Int
)