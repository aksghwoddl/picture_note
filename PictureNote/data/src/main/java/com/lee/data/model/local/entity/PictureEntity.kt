package com.lee.data.model.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lee.domain.common.DomainUtils
import com.lee.domain.model.remote.Picture

/**
 * 즐겨찾기 Entity class
 * **/
@Entity(tableName = DomainUtils.TABLE_NAME)
data class PictureEntity(
    @PrimaryKey val id : String,
    val picture : Picture,
    val index : Int
)