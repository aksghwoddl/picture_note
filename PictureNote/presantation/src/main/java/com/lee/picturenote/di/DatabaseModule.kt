package com.lee.picturenote.di

import android.content.Context
import androidx.room.Room
import com.lee.picturenote.common.DB_NAME
import com.lee.picturenote.data.local.dao.PictureDAO
import com.lee.picturenote.data.local.db.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database 관련 Module (Room)
 * **/
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providePictureDAO(database: FavoriteDatabase) : PictureDAO {
        return database.pictureDAO()
    }

    @Provides
    @Singleton
    fun provideFavoriteDatabase(@ApplicationContext context : Context) : FavoriteDatabase {
        return Room.databaseBuilder(
            context ,
            FavoriteDatabase::class.java ,
            DB_NAME
        ).build()
    }
}