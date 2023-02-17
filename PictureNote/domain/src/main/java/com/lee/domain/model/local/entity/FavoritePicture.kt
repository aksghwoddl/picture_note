package com.lee.domain.model.local.entity

import com.lee.domain.model.remote.Picture

/**
 * Room으로 부터 받은 즐겨찾기 목록을 담는 holder class
 * **/
data class FavoritePicture(
    val id : String,
    val picture: Picture,
    var index : Int
)