package com.lee.data.mapper

import com.lee.data.model.local.entity.PictureEntity
import com.lee.data.model.remote.PictureDTO
import com.lee.domain.model.local.FavoritePicture
import com.lee.domain.model.remote.Picture

object DataMapper {
    fun mapperToPictureList(list : MutableList<PictureDTO>) : MutableList<Picture>{
        val parsingList = mutableListOf<Picture>()
        list.forEach {
            parsingList.add(
               it.run {
                   Picture(id, author, width, height, downloadUrl, isFavorite)
               }
            )
        }
        return parsingList
    }

    fun mapperToPicture(pictureDTO: PictureDTO) : Picture {
        val picture : Picture = pictureDTO.run {
            Picture( id, author, width, height, downloadUrl , isFavorite)
        }
        return picture
    }

    fun mapperToPictureEntity(favoritePicture: FavoritePicture) : PictureEntity {
        val pictureEntity = favoritePicture.run {
            PictureEntity(id, picture, index)
        }
        return pictureEntity
    }
}