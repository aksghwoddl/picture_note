package com.lee.picturenote.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.picturenote.common.TABLE_NAME
import com.lee.picturenote.data.remote.model.Picture

/**
 * 즐겨찾기 Entity class
 * **/
@Entity(tableName = TABLE_NAME)
data class PictureEntity(
    @PrimaryKey val id : String ,
    val picture : Picture ,
    val index : Int
)