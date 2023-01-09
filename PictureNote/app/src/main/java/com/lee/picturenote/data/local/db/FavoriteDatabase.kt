package com.lee.picturenote.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lee.picturenote.data.local.dao.PictureDAO
import com.lee.picturenote.data.local.entity.PictureEntity
import com.lee.picturenote.data.local.typeconverter.TypeConverter

/**
 * RoomDatabase class
 * **/
@Database(entities = [PictureEntity::class] , version = 1 , exportSchema = true)
@TypeConverters(TypeConverter::class)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun pictureDAO() : PictureDAO
}