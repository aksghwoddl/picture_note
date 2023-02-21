package com.lee.data.model.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lee.data.model.local.TypeConverter
import com.lee.data.model.local.dao.PictureDAO
import com.lee.data.model.local.entity.PictureEntity

/**
 * RoomDatabase class
 * **/
@Database(entities = [PictureEntity::class] , version = 1 , exportSchema = true)
@TypeConverters(TypeConverter::class)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun pictureDAO() : PictureDAO
}