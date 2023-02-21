package com.lee.data.model.local.dao

import androidx.room.*
import com.lee.domain.model.local.FavoritePicture
import com.lee.data.model.local.entity.PictureEntity

/**
 * 즐겨찾기 DAO interface
 * **/
@Dao
interface PictureDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoritePicture(pictureEntity: PictureEntity)

    @Update
    suspend fun updatePicture(pictureEntity: PictureEntity)

    @Delete
    suspend fun deleteFavoritePicture(pictureEntity: PictureEntity)

    @Query("SELECT * FROM favorite_tbl ORDER BY `index`")
    suspend fun getFavoritePicture() : MutableList<FavoritePicture>

    @Query("SELECT count(*) FROM favorite_tbl")
    suspend fun getFavoritePictureCount() : Int

    @Query("SELECT `index` FROM favorite_tbl WHERE `id` =:id" )
    suspend fun getIndexById(id : String) : Int

    @Query("SELECT * FROM favorite_tbl WHERE id =:id")
    suspend fun getFavoritePictureById(id : String) : FavoritePicture?
}